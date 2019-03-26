/**
 * 
 */
package com.example.demo.serviceimpl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import com.example.demo.entity.File;
import com.example.demo.mapper.FileMapper;
import com.example.demo.service.FileService;

/**
* @author Li Yang
* @version vb1.0
* @email 1246457819@qq.com
* @date 2019年3月25日 下午8:52:31
*/
/**
 * @author Administrator
 *
 */
@ComponentScan({"com.example.demo.mapper"})
@Service
public class FileServiceImpl implements FileService {

	/* (non-Javadoc)
	 * @see com.example.demo.service.FileService#insert(com.example.demo.entity.File)
	 */
	@Autowired
	private FileMapper fileMapper;
	@Override
	public int insert(File file) {
		// TODO Auto-generated method stub
		return fileMapper.insertSelective(file);
	}

}
