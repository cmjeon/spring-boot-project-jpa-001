package com.shop.demoShop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {
  public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
    UUID uuid = UUID.randomUUID(); // UUID(Universally Unique Identifier) 중복이 없는 식별자를 생성할 때 사용
    String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
    String savedFileName = uuid.toString() + extension; // UUID 와 확장자를 조합
    String fileUploadFullUrl = uploadPath + "/" + savedFileName;
    FileOutputStream fos = new FileOutputStream(fileUploadFullUrl); // 파일이 저장될 위치와 이름으로 출력스트림 생성
    fos .write(fileData); // fileData 를 파일 출력 스트림에 입력
    fos.close();
    return savedFileName; // 업로드된 파일 이름을 반환
  }
  public void deleteFile(String filePath) throws Exception{
    File deleteFile = new File(filePath); // 파일이 저장된 경로를 이용하여 파일 객체 생성
    if(deleteFile.exists()) {  // 해당 파일이 존재하면 삭제
      deleteFile.delete();
      log.info("파일을 삭제하였습니다•");
    } else {
      log.info("파일이 존재하지 않습니다.");
    }
  }
}