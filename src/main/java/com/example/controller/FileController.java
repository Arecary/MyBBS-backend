package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.example.common.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * 文件接口
 */
@RestController
@RequestMapping("/files")
public class FileController {

  @Autowired
  private AmazonS3 s3Client;

  @Value("${application.bucket.name}")
  private String bucketName;

  /**
   * 文件上传到S3
   */
  @PostMapping("/upload")
  public Result upload(MultipartFile file) {
    String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
    try {
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentLength(file.getSize());
      s3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);
      System.out.println(fileName + "--上传成功");
    } catch (IOException e) {
      System.err.println(fileName + "--文件上传失败");
    }
    String fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
    return Result.success(fileUrl);
  }

  /**
   * 富文本文件上传
   */
  @PostMapping("/editor/upload")
  public Dict editorUpload(MultipartFile file) {
    String flag = System.currentTimeMillis() + "";
    String fileName = flag + "-" + file.getOriginalFilename();
    try {
      // 上传文件到S3
      File tempFile = convertMultiPartToFile(file);
      s3Client.putObject(new PutObjectRequest(bucketName, fileName, tempFile));
      tempFile.delete(); // 删除临时文件

      String fileUrl = s3Client.getUrl(bucketName, fileName).toString();
      System.out.println(fileName + "--上传成功");

      return Dict.create().set("errno", 0).set("data", CollUtil.newArrayList(Dict.create().set("url", fileUrl)));
    } catch (Exception e) {
      System.err.println(fileName + "--文件上传失败");
      return Dict.create().set("errno", 1).set("msg", "上传失败");
    }
  }

  private File convertMultiPartToFile(MultipartFile file) throws IOException {
    Path tempDir = Files.createTempDirectory("");
    File tempFile = new File(tempDir.toFile(), file.getOriginalFilename());
    file.transferTo(tempFile);
    return tempFile;
  }



  /**
   * 从S3获取文件
   */
  @GetMapping("/{fileName}")
  public void downloadFile(@PathVariable String fileName, HttpServletResponse response) {
    try {
      S3Object s3Object = s3Client.getObject(bucketName, fileName);
      response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
      response.setContentType("application/octet-stream");
      org.apache.commons.io.IOUtils.copy(s3Object.getObjectContent(), response.getOutputStream());
      response.flushBuffer();
    } catch (IOException e) {
      System.out.println("文件下载失败");
    }
  }

  /**
   * 从S3删除文件
   */
  @DeleteMapping("/{fileName}")
  public Result deleteFile(@PathVariable String fileName) {
    s3Client.deleteObject(bucketName, fileName);
    System.out.println("删除文件" + fileName + "成功");
    return Result.success("删除成功");
  }


}
