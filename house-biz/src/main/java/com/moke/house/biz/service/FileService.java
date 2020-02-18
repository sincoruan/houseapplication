package com.moke.house.biz.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import com.google.common.io.Files;

@Service
public class FileService {
    @Value("${file.path:}")
    private String filePath;
    public List<String> getImagePath(List<MultipartFile> files ){
        List<String> paths =  new ArrayList<>();
        files.forEach(file->{
            File localfile = null;
            if(!file.isEmpty()){
                try{
                    localfile = saveToLocal(file,filePath);
                    String path = StringUtils.substringAfterLast(localfile.getAbsolutePath(), filePath);
                    paths.add(path);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        return paths;
    }
    private File saveToLocal(MultipartFile file,String path)throws IOException {
        File newFile  = new File(path+"/"+ Instant.now().getEpochSecond()+"/"+file.getOriginalFilename());
        if(!newFile.exists()){
            newFile.getParentFile().mkdirs();
            newFile.createNewFile();
        }
        Files.write(file.getBytes(),newFile);
        return newFile;
    }
}
