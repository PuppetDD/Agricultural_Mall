package com.gold.mall.service.impl;

import com.gold.mall.common.utils.FtpUtils;
import com.gold.mall.common.utils.SolrUtil;
import com.gold.mall.common.utils.StringUtil;
import com.gold.mall.dao.ProductDao;
import com.gold.mall.dto.ProductDto;
import com.gold.mall.dto.ProductSolrDto;
import com.gold.mall.params.ProductParam;
import com.gold.mall.pojo.Product;
import com.gold.mall.pojo.ProductType;
import com.gold.mall.service.ProductService;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author GOLD
 * @date 2020/2/23 18:04
 * Description <商品impl>
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Value("${ftp.host}")
    private String host;

    @Value("${ftp.port}")
    private Integer port;

    @Value("${ftp.username}")
    private String username;

    @Value("${ftp.password}")
    private String password;

    @Value("${ftp.basePath}")
    private String basePath;

    @Value("${ftp.path}")
    private String path;

    @Value("${ftp.baseUrl}")
    private String baseUrl;

    /**
     * @param [productDto]
     * @return {@link int}
     * Description <添加商品 Solr更新>
     * @author GOLD
     * @date 2020/2/24 13:33
     */
    @Override
    public int addProduct(ProductDto productDto) throws FileUploadException {
        //1.文件上传
        //String fileName = StringUtil.reFileName(productDto.getFileName());
        //String filePath = productDto.getUploadPath()+"\\"+ fileName;
        //
        //try {
        //    StreamUtils.copy(productDto.getInputStream(),new FileOutputStream(filePath));
        //} catch (IOException e) {
        //    throw new FileUploadException("文件上传失败"+e.getMessage());
        //}

        //将文件上传ftp服务器上
        //按照时间创建文件夹
        String timePath = new SimpleDateFormat("yyyyMMdd").format(new Date());

        String filePath = path + "/" + timePath;
        String fileName = StringUtil.reFileName(productDto.getFileName());
        boolean flag = FtpUtils.uploadFile(host, port, username, password, basePath, filePath, fileName, productDto.getInputStream());

        if (!flag) {
            throw new FileUploadException("文件上传失败");
        }

        //2.保存到数据库,将 dto 转化为 pojo
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        //product.setImage(filePath);
        //地址修改为服务器上的http地址
        //product.setImage(baseUrl+"/" + filePath + "/" + fileName);
        product.setImage(baseUrl + "/" + timePath + "/" + fileName);

        ProductType productType = new ProductType();
        productType.setId(productDto.getProductTypeId());

        product.setProductType(productType);
        int row = productDao.insertProduct(product);
        if (row >= 1) {
            product = productDao.selectByProductName(product.getName());
            ProductSolrDto productSolrDto = new ProductSolrDto();
            productSolrDto.copyProperties(product);
            SolrUtil.saveSolrResource(productSolrDto);
        }
        return row;
    }

    /**
     * @param [name]
     * @return {@link Boolean}
     * Description <检测商品名称是否可用>
     * @author GOLD
     * @date 2020/2/24 13:33
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Boolean checkProductName(String name) {
        Product product = productDao.selectByProductName(name);
        if (product != null) {
            //商品名称已经存在
            return false;
        }
        return true;
    }

    /**
     * @param []
     * @return {@link List< Product>}
     * Description <查找所有商品>
     * @author GOLD
     * @date 2020/2/24 13:33
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public List<Product> findAllProducts() {
        return productDao.selectAllProducts();
    }

    /**
     * @param [id]
     * @return {@link Product}
     * Description <根据 id 查找商品>
     * @author GOLD
     * @date 2020/2/24 13:33
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Product findProductById(int id) {
        return productDao.selectProductById(id);
    }

    /**
     * @param [productDto]
     * @return {@link int}
     * Description <修改商品 Solr更新>
     * @author GOLD
     * @date 2020/2/24 13:33
     */
    @Override
    public int modifyProduct(ProductDto productDto) throws FileUploadException {
        //1.文件上传
        //String fileName = StringUtil.reFileName(productDto.getFileName());
        //String filePath = productDto.getUploadPath()+"\\"+ fileName;
        //
        //try {
        //    StreamUtils.copy(productDto.getInputStream(),new FileOutputStream(filePath));
        //} catch (IOException e) {
        //    throw new FileUploadException("文件上传失败"+e.getMessage());
        //}

        String imagePath = null;
        if (!productDto.getFileName().isEmpty()) {
            String timePath = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String filePath = path + "/" + timePath;
            String fileName = StringUtil.reFileName(productDto.getFileName());

            boolean flag = FtpUtils.uploadFile(host, port, username, password, basePath, filePath, fileName, productDto.getInputStream());

            if (!flag) {
                throw new FileUploadException("文件上传失败");
            } else {
                imagePath = baseUrl + "/" + timePath + "/" + fileName;
            }
        }
        //2.保存到数据库,将 dto 转化为 pojo
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        //product.setImage(filePath);
        //https://120.77.212.201/images/20190301512474268.jpg
        product.setImage(imagePath);

        ProductType productType = new ProductType();
        productType.setId(productDto.getProductTypeId());

        product.setProductType(productType);
        int row = productDao.updateProduct(product);
        if (row >= 1) {
            ProductSolrDto productSolrDto = new ProductSolrDto();
            productSolrDto.copyProperties(product);
            SolrUtil.saveSolrResource(productSolrDto);
        }
        return row;
    }

    /**
     * @param [id]
     * @return {@link int}
     * Description <删除商品 Solr更新>
     * @author GOLD
     * @date 2020/2/24 13:34
     */
    @Override
    public int removeProductById(int id) {
        int row = productDao.deleteProductById(id);
        if (row >= 1) {
            SolrUtil.removeSolrData(String.valueOf(id));
        }
        return row;
    }

    /**
     * @param [path, outputStream]
     *               Description <获取图片，将图片响应到输出流中，显示图片>
     * @author GOLD
     * @date 2020/2/24 13:34
     */
    @Override
    public void getImage(String path, OutputStream outputStream) {
        try {
            StreamUtils.copy(new FileInputStream(path), outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param [productParam]
     * @return {@link List< Product>}
     * Description <多条件查找商品 Solr全文检索>
     * @author GOLD
     * @date 2020/2/24 13:34
     */
    @Override
    public List<Product> findByProductParams(ProductParam productParam) {
        List<Product> productList = new ArrayList<>();
        try {
            QueryResponse response = SolrUtil.query(productParam.toString());
            List<ProductSolrDto> solrDto = response.getBeans(ProductSolrDto.class);
            //SolrDocumentList solrDocumentList = response.getResults();
            Product product;
            //ProductType productType;
            for (ProductSolrDto productSolrDto : solrDto) {
                product = new Product();
                productSolrDto.giveProperties(product);
                productList.add(product);
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return productList;
    }

}
