package com.huiming.service;

import org.springframework.web.multipart.MultipartFile;

/*
 * @author:15737
 * @createtime:2017/11/11 16:04
 * @desc:
*/
public interface IFileService {
    String upload(MultipartFile file, String path);
}
