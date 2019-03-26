/**
 * 
 */
package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;

/**
* @author Li Yang
* @version vb1.0
* @email 1246457819@qq.com
* @date 2019年3月25日 下午7:55:05
*/
@Configuration
@EnableMBeanExport(registration=RegistrationPolicy.IGNORE_EXISTING)
public class FdfsConfiguration {

}
