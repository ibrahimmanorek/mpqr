package com.paypro.qrcode.controller;

import com.paypro.qrcode.csvmodel.Emvco;
import com.paypro.qrcode.csvmodel.QrModel;
import com.paypro.qrcode.service.QrService;
import com.paypro.qrcode.utill.ConstantsManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class QrCodeController {

    private static final Logger log = Logger.getLogger(QrCodeController.class);

    @Autowired
    private QrService qrService;

    private PagedListHolder pagedListHolder;
    private List<QrModel> qrModels;

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ModelAndView uploadFile(MultipartFile file, HttpServletRequest req) {
        log.info("===Post Upload File===");
        if (0 != file.getSize()) {
            File rootPath = new File(req.getSession().getServletContext().getRealPath(
                    File.separator + ConstantsManager.Path.ASSETS + File.separator));
            List<Emvco> emvcos = qrService.uploadEmvcoQrHandler(file, rootPath);
            qrModels = getEmvcoQrImage(emvcos);

            pagedListHolder = new PagedListHolder<>(qrModels);
            pagedListHolder.setPageSize(ConstantsManager.Page.MAX_PAGE);

            return new ModelAndView(ConstantsManager.Page.INDEX, createModelMap());
        } else {
            return new ModelAndView(ConstantsManager.Page.INDEX);
        }
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.GET)
    public ModelAndView uploadFile() {
        log.info("===Get Upload File===");
        return new ModelAndView(ConstantsManager.Page.INDEX, createModelMap());
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() {
        log.info("===Get Index===");
        return new ModelAndView(ConstantsManager.Page.INDEX);
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response, HttpServletRequest request) throws IOException {
        log.info("===Get Download File===");
        File rootPath = new File(request.getSession().getServletContext().getRealPath(
                File.separator + ConstantsManager.Path.ASSETS));
        qrService.downloadHandler(response, rootPath, "emvcoexample.xlsx");
    }

    @RequestMapping(value = "/downloadQr", method = RequestMethod.GET)
    public void downloadQrImage(HttpServletResponse response, HttpServletRequest request) throws IOException {
        log.info("===Get Download QR Image===");
        File rootPath = new File(request.getSession().getServletContext().getRealPath(File.separator));
        qrService.downloadQrImageHandler(request, response, rootPath, qrModels);
    }

    @RequestMapping(value = "/qrImages", method = RequestMethod.GET)
    public ModelAndView pagination(@RequestParam(required = false) Integer page) {
        log.info("===Get Pagination===");
        ModelAndView modelAndView = new ModelAndView("redirect:/uploadFile");

        if (null == page || page < 1 || page > pagedListHolder.getPageCount())
            page = 1;

        modelAndView.addObject("page", page);

        if (page < 1 || page > pagedListHolder.getPageCount()) {
            pagedListHolder.setPage(0);
            modelAndView.addObject(ConstantsManager.ObjectView.LIST_OF_QRMODEL, pagedListHolder.getPageList());
        } else if (page <= pagedListHolder.getPageCount()) {
            pagedListHolder.setPage(page - 1);
            modelAndView.addObject(ConstantsManager.ObjectView.LIST_OF_QRMODEL, pagedListHolder.getPageList());
        }
        return modelAndView;
    }

    private List<QrModel> getEmvcoQrImage(List<Emvco> emvcos) {
        List<QrModel> models = new ArrayList<>();
        String path = ConstantsManager.Path.ASSETS + File.separator + ConstantsManager.Path.IMG + File.separator;
        int i = 0;
        for (Emvco emvco : emvcos) {
            i++;
            QrModel qrModel = new QrModel();
            qrModel.setImagePath(path + i + "_" + emvco.getName() + ".png");
            qrModel.setImageName(i + "_" + emvco.getName());
            models.add(qrModel);
        }
        return models;
    }

    private ModelMap createModelMap() {
        ModelMap modelMap = new ModelMap();
        modelMap.put("qrModels", pagedListHolder.getPageList());
        modelMap.put("maxPages", pagedListHolder.getPageCount());
        return modelMap;
    }
}
