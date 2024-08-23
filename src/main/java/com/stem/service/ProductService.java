package com.stem.service;

import com.stem.dao.TblZDemoProductMapper;
import com.stem.dao.TblZDemoTransactionMapper;
import com.stem.po.TblZDemoProduct;
import com.stem.po.TblZDemoTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private final TblZDemoProductMapper tblZDemoProductMapper;
    @Autowired
    private final TblZDemoTransactionMapper tblZDemoTransactionMapper;

    /**
     * 更新当前库存
     * @param tblZDemoProduct
     * @return
     */
    public int updateTblZDemoInventory(TblZDemoProduct tblZDemoProduct){
       return tblZDemoProductMapper.updateTblZDemoProductCurQuantity(tblZDemoProduct);
    }

    public void insertReqeust(TblZDemoTransaction tblZDemoTransaction){
        tblZDemoTransactionMapper.insertRequest(tblZDemoTransaction);
    }

    /**
     * 获取Request表最大的
     * @param productNo
     * @return
     */
    public TblZDemoTransaction getMaxRequest(String  productNo){
        return tblZDemoTransactionMapper.getMaxTblZDemoTransactionByProductNo(productNo);
    }

    public void deleteProduct(String product_no){
        tblZDemoProductMapper.deleteProduct(product_no);
    }

    public void insertProduct(TblZDemoProduct tblZDemoInventory){
        tblZDemoProductMapper.insertProduct(tblZDemoInventory);
    }

    public List<TblZDemoProduct> getTblZDemoProduct(){
        return tblZDemoProductMapper.getAllTblZDemoIn();
    }
    public TblZDemoProduct getTblZDemoProductByProductNo(String productNo){
        return tblZDemoProductMapper.getTblZDemoInventoryByProductNo(productNo);
    }
    public TblZDemoProduct getTblZDemoProductById(Long id){
        return tblZDemoProductMapper.getTblZDemoProductByID(id);
    }

    public TblZDemoTransaction getMaxTblZDemoTransactionByProductNo(String productNo){
        return tblZDemoTransactionMapper.getMaxTblZDemoTransactionByProductNo(productNo);
    }

    @Autowired
    public ProductService(TblZDemoProductMapper tblZDemoProductMapper, TblZDemoTransactionMapper tblZDemoTransactionMapper){
        this.tblZDemoProductMapper = tblZDemoProductMapper;
        this.tblZDemoTransactionMapper = tblZDemoTransactionMapper;
    }

}
