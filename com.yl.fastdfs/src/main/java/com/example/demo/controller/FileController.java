/**
 * 
 */
package com.example.demo.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;

import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.File;
import com.example.demo.service.FileService;
import com.example.demo.utils.CommonFileUtil;

/**
* @author Li Yang
* @version vb1.0
* @email 1246457819@qq.com
* @date 2019年3月25日 下午8:00:58
*/
@Controller
public class FileController {
	
	private final static Logger logger = LoggerFactory.getLogger(FileController.class);
	
	@Autowired
	private CommonFileUtil fileUtil;
	@Autowired
	private FileService fileService;
	@RequestMapping("/goIndex")
	public String goIndex(){
		logger.info("进入主页面");
		return "file";
	}
	
	@RequestMapping("/fileUpload")
	public String fileUpload(@RequestParam("fileName") MultipartFile file){
		
		String targetFilePath = "E:/opt/uploads/";
		
		if(file.isEmpty()){
			logger.info("this file is empty");
		}
		
		String newFileName = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		//获取原来文件名称
		String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		
		if(!fileSuffix.equals(".jpg") || !fileSuffix.equals(".png")){
			logger.info("文件格式不正确");
		}
		//拼装新的文件名
		String targetFileName = targetFilePath + newFileName + fileSuffix;
		//上传文件
		try {
			FileCopyUtils.copy(file.getInputStream(),new FileOutputStream(targetFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "/success";
	}
	
	//使用fastdfs进行文件上传
	@RequestMapping(value="/uploadFileToFast",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String uoloadFileToFast(@RequestParam("fileName")MultipartFile file,@RequestParam("name")String name) throws IOException{
		JSONObject result = new JSONObject();
		if(file.isEmpty()){
			logger.info("文件不存在");
		}
		String path = null;
		try {
		path = fileUtil.uploadFile(file);
		File db_file = new File();
		System.out.println(name);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = simpleDateFormat.format(new Date());
    	System.out.println(date);
		db_file.setName(name);
		db_file.setPath("http://yourip:80/"+path);
		db_file.setCreateTime(date);
		
		fileService.insert(db_file);
		}catch (Exception e) {
			// TODO: handle exception
			logger.info("上传文件失败，有可能文件名重复或者连接超时！");
			e.printStackTrace();
			fileUtil.deleteFile(path);
			
			result.put("code", "500");
			if(e.toString().contains("DuplicateKeyException")) {
				result.put("msg", "文件上传失败！重复的文件名！");
			}else {
				result.put("msg", "文件上传失败！"+e.toString());
			}
			
			return result.toJSONString();
		}
		System.out.println(path);
		
		result.put("code", "200");
		result.put("msg", "文件上传成功！");
		result.put("path", "http://yourip:80/"+path);
	
		return result.toJSONString();
	}

}
