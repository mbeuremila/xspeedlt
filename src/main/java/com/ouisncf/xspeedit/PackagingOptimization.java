package com.ouisncf.xspeedit;

import java.util.List;

/**
 * Interface for the packaging optimization algorithm
 */
public interface PackagingOptimization {

    /**
     * Package article in boxes
     * @param chainOfArticleToPackage the chain of article to package
     * @return the list of article packaged in boxes
     * @throws BusinessException
     */
    List<Box> packageArticle(String chainOfArticleToPackage) throws BusinessException;
}
