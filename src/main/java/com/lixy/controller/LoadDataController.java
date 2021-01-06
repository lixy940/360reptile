package com.lixy.controller;


import com.lixy.service.ReptileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author MR LIS
 * @since 2020-04-07
 */
@Api(tags = "装载数据接口管理")
@Validated
@RestController
@RequestMapping("/load")
@Slf4j
public class LoadDataController {



    @Autowired
    private ReptileService reptileService;

    /**
     * 关键词
     * @param
     * @return
     */
    @ApiOperation(value = "关键词")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "search", dataType = "String", required = true, value = "关键词", defaultValue = "")
    })
    @GetMapping("/reptile")
    public void reptile(String search,HttpServletResponse response){
        String fileName = "biddingwords.txt";
        OutputStream os = null;
        try {
            String result = reptileService.climber(search);
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(),"ISO8859-1"));
            byte[] bytes = result.getBytes("utf8");
            os = response.getOutputStream();
            // 将字节流传入到响应流里,响应到浏览器
            os.write(bytes);
//            os.close();
        } catch (Exception ex) {
            log.error("导出失败:", ex);
            throw new RuntimeException("导出失败");
        }finally {
            try {
                if (null != os) {
                    os.close();
                }
            } catch (IOException ioEx) {
                log.error("导出失败:", ioEx);
            }
        }

    }


}
