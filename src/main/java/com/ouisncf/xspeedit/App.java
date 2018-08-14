package com.ouisncf.xspeedit;

import lombok.Getter;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Main class of the app
 */
@Getter
public class App {

    /**
     * Main method to launch the app
     * @param args arguments passed to the app
     */
    public static void main(String[] args) {

        PackagingOptimization packagingLogic = new PackagingOptimizationImpl();


        Scanner sc = new Scanner(System.in);
        System.out.println(ConstantMessage.USER_INPUT_INFO);
        String chainOfArticleToPackage = sc.nextLine();

        // Display the user input
        System.out.println(String.format("Chaîne d'articles en entrée : %s", chainOfArticleToPackage));


        List<Box> boxList = null;
        try {
            // Calculate the distribution of articles in boxes
            boxList = packagingLogic.packageArticle(chainOfArticleToPackage);
            // Display boxes
            displayResultToUser(boxList);
        } catch (BusinessException e) {
           System.out.println(e.getMessage());
        }

    }

    /**
     * Transform a list of box in string. A separator is present between each box.
     * @param boxList list of box to display
     */
    private static void displayResultToUser(List<Box> boxList){
        String str = boxList.stream().map(Box::toString)
                .collect(Collectors.joining(Constants.DISPLAY_SEPARATOR));
        System.out.println(String.format("Chaîne d'articles emballés : %s   => %d  cartons utilisés", str, boxList.size()));
    }
}
