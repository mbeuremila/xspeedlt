package com.ouisncf.xspeedit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class for the packaging optimization algorithm
 * The algorithm sort article by size in descending order.
 * Then it searches boxes (or create it) with the capacity to accept it.
 * Finally, it place the article in the box with the less free space
 *
 */
public class PackagingOptimizationImpl implements PackagingOptimization {


    /**
     * {@inheritDoc}
     */
    public List<Box> packageArticle(String chainOfArticleToPackage) throws BusinessException {

        validateChainArticle(chainOfArticleToPackage);

        List<Integer> itemListToPackage = chainArticleToArticle(chainOfArticleToPackage);

        List<Box> boxList = new ArrayList<>();
        for (Integer item : itemListToPackage) {

            Box box = getBox(boxList, item);
            if (CollectionUtils.isEmpty(box.getItems())) {
                boxList.add(box);
            }
            box.getItems().add(item);

        }

        return boxList;
    }

    /**
     * Validate the format of the chain of the article
     * @param chainOfArticleToPackage chain of the article given by the user
     * @throws BusinessException
     */
    private void validateChainArticle(String chainOfArticleToPackage) throws BusinessException {

        if (StringUtils.isEmpty(chainOfArticleToPackage)) {
            throw new BusinessException(ConstantMessage.CHAIN_ARTICLE_EMPTY);
        }

        if (!chainOfArticleToPackage.matches("^[1-9]+$")) {
            throw new BusinessException(ConstantMessage.CHAIN_ARTICLE_BAD_FORMAT);
        }
    }

    /**
     * Transform the chain of article in a list of article
     * @param stringToTransform the chain of article
     * @return a list of article
     */
    private List<Integer> chainArticleToArticle(String stringToTransform) {
        return Stream.of(stringToTransform.split(Constants.INPUT_CHAIN_ARTICLE_SEPARATOR))
                .map(Integer::parseInt)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    /**
     * Get a box with the capacity to accept the article in parameter or a new box if none exists
     * @param boxList the list of boxes
     * @param article article to place in a box
     * @return the box which will accept the article
     */
    private Box getBox(List<Box> boxList, Integer article) {
        return boxList.stream()
                .filter(b -> b.getRemainingCapacity() >= article)
                .min(Comparator.comparing(Box::getRemainingCapacity))
                .orElseGet(Box::new);
    }

}
