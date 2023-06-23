package himedia.oneshot.service;

import himedia.oneshot.entity.Product;
import himedia.oneshot.repository.JdbcAdminProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class AdminProductService {

    private final JdbcAdminProductRepository adminProductRepository;

    private Product saveImage(Product product,MultipartFile thumbImgFile, MultipartFile[] expImgFiles) throws Exception{
//        String thumbImgName = thumbImgFile.getOriginalFilename();
        UUID thumbName = UUID.randomUUID();
        UUID uuid = UUID.randomUUID();
        String thumbImgName = thumbName + ".jpg";
//        List<String> expImgNames = new ArrayList<>();
//        for(MultipartFile img : expImgFiles){
//            String imgName = img.getOriginalFilename();
//            expImgNames.add(imgName);
//        }

        List<String> expImgNames = new ArrayList<>();
        for(MultipartFile img : expImgFiles){
            String imgName = uuid + img.getOriginalFilename();
            expImgNames.add(imgName);
        }
//        String thumbPath = System.getProperty("user.dir") +
//                "/src/main/resources/static/img/product/thumbnail";

        String thumbPath = new ClassPathResource("/img/product/thumbnail").getPath();

//        String expPath = System.getProperty("user.dir")+
//                "/src/main/resources/static/img/product/explanation";
        String expPath = new ClassPathResource("/img/product/explanation").getPath();

        File thumbSaveFile = new File(thumbPath,thumbImgName);
        thumbImgFile.transferTo(thumbSaveFile);

        for (int i = 0; i < expImgFiles.length; i++) {
            if (i < expImgNames.size() && !expImgNames.get(i).isEmpty()) {
                File expSaveFile = new File(expPath, expImgNames.get(i));
                expImgFiles[i].transferTo(expSaveFile);
            }
        }
        product.setImg_thumb("img/product/thumbnail/"+thumbImgName);
        product.setImg_exp1("img/product/explanation/"+expImgNames.get(0));
        if (expImgNames.size() > 1 && !expImgNames.get(1).isEmpty()) {
            product.setImg_exp2("img/product/explanation/" + expImgNames.get(1));
        }
        return product;
    }


    public void saveProduct(Product product, MultipartFile thumbImgFile, MultipartFile[] expImgFiles)throws Exception{
        saveImage(product,thumbImgFile, expImgFiles);
        adminProductRepository.saveProduct(product);
    }
    public void updateProduct(Long id,Product updatedProduct,MultipartFile thumbImgFile, MultipartFile[] expImgFiles) throws Exception {
        if ((thumbImgFile == null || thumbImgFile.isEmpty()) || (expImgFiles == null || expImgFiles.length == 0)) {
            Product existingProduct = adminProductRepository.findById(id).get();
            updatedProduct.setImg_thumb(existingProduct.getImg_thumb());
            updatedProduct.setImg_exp1(existingProduct.getImg_exp1());
            updatedProduct.setImg_exp2(existingProduct.getImg_exp2());
        }else {
            saveImage(updatedProduct,thumbImgFile,expImgFiles);
        }
        adminProductRepository.updateProduct(id, updatedProduct);
    }

    public void updateProductStatus(Long id, String status){
        adminProductRepository.updateProductStatus(id,status);
    }

    public Optional<Product> findById(Long id){
        return adminProductRepository.findById(id);
    }

    public List<Product> findAllAdmin(){
        return  adminProductRepository.findAllAdmin();
    }


}