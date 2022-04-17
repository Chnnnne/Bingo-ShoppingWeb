package com.leyou.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;

@Service
public class GoodsHtmlService {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private TemplateEngine templateEngine;


    /**
     * 创建html页面
     *
     * @param spuId
     * @throws Exception
     */
    public void createHtml(Long spuId) {

        PrintWriter writer = null;
        try {
            // 获取页面数据
            Map<String, Object> spuMap = this.goodsService.loadData(spuId);

            // 创建thymeleaf上下文对象
            Context context = new Context();
            // 把数据放入上下文对象
            context.setVariables(spuMap);

            // 创建输出流
            File file = new File("D:\\develop\\nginx-1.20.2\\html\\item\\" + spuId + ".html");
            writer = new PrintWriter(file);

            // 执行页面静态化方法
            templateEngine.process("item", context, writer);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public void deleteHtml(Long id) {
        File file = new File("D:\\develop\\nginx-1.20.2\\html\\item\\" + id + ".html");
        file.deleteOnExit();
    }

//    /**
//     * 新建线程处理页面静态化
//     * @param spuId
//     */
//    public void asyncExcute(Long spuId) {
//        ThreadUtils.execute(()->createHtml(spuId));
//        /*ThreadUtils.execute(new Runnable() {
//            @Override
//            public void run() {
//                createHtml(spuId);
//            }
//        });*/
//    }
}