package com.csq.lihao.Controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Value("${file.upload-dir:./uploads/}")
    private String uploadDir;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        // 1. 使用Hutool的FileUtil创建上传目录（如果不存在）
        File dir = FileUtil.mkdir(uploadDir);

        // 2. 获取原始文件名和扩展名，使用Hutool的IdUtil生成UUID新文件名
        String originFilename  = file.getOriginalFilename();
        String extName = FileUtil.extName(originFilename);
        String newFileName = IdUtil.fastSimpleUUID()+"."+ extName;

        // 3. 构建目标文件路径
        File dest = FileUtil.file(dir,newFileName);

        // 4. 保存文件
        file.transferTo(dest);
        log.info("文件上传成功: {} -> {}", originFilename, newFileName);

        // 5. 返回文件名供后续下载
        return newFileName;
    }

    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        try {
            File file = FileUtil.file(uploadDir,fileName);
            if(file == null){
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return ;
            }
            // 设置响应头，处理中文文件名乱码
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename="
                    + URLEncoder.encode(fileName, "UTF-8"));
            // 使用Hutool的FileUtil直接写出文件流
            FileUtil.writeToStream(file, response.getOutputStream());
        }catch(IOException e){
            log.error("文件下载失败", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }


    }

}
