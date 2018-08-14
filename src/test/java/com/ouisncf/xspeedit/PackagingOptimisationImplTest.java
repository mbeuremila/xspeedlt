package com.ouisncf.xspeedit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.never;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PackagingOptimization.class, PackagingOptimizationImpl.class})
public class PackagingOptimisationImplTest {

    /** Message for an unexpected exception*/
    private static final String UNEXPECTED_EXCEPTION = "We shouldn't have an exception : ";
    /** Message for an expected exception */
    private static final String EXCEPTION_EXPECTED = "We should have an exception : ";

    /** Service to test */
    @InjectMocks
    @Spy
    private PackagingOptimization service = new PackagingOptimizationImpl();

    /**
     * Case where articles are packed in boxes
     */
    @Test
    public void shouldPackageArticle() {

        String chainArticleToArticle = "163841689525773";

        try {
            PowerMockito.doNothing().when(service, "validateChainArticle", chainArticleToArticle);
            PowerMockito.doReturn(Arrays.asList(9, 8, 8, 7, 7, 6, 6, 5, 5, 4, 3, 3, 2, 1, 1)).when(service, "chainArticleToArticle", chainArticleToArticle);

            List<Box> list = new ArrayList<>();
            PowerMockito.doReturn(new Box()).when(service, "getBox", list, 9);
            PowerMockito.doReturn(new Box()).when(service, "getBox", list, 8);
            PowerMockito.doReturn(new Box()).when(service, "getBox", list, 8);
            PowerMockito.doReturn(new Box()).when(service, "getBox", list, 7);
            PowerMockito.doReturn(new Box()).when(service, "getBox", list, 7);
            PowerMockito.doReturn(new Box()).when(service, "getBox", list, 6);
            PowerMockito.doReturn(new Box()).when(service, "getBox", list, 6);
            PowerMockito.doReturn(new Box()).when(service, "getBox", list, 5);
            PowerMockito.doReturn(Box.builder().items(Arrays.asList(5)).build()).when(service, "getBox", list, 5);
            PowerMockito.doReturn(Box.builder().items(Arrays.asList(6)).build()).when(service, "getBox", list, 4);
            PowerMockito.doReturn(Box.builder().items(Arrays.asList(7)).build()).when(service, "getBox", list, 3);
            PowerMockito.doReturn(Box.builder().items(Arrays.asList(7)).build()).when(service, "getBox", list, 3);
            PowerMockito.doReturn(Box.builder().items(Arrays.asList(8)).build()).when(service, "getBox", list, 2);
            PowerMockito.doReturn(Box.builder().items(Arrays.asList(9)).build()).when(service, "getBox", list, 1);
            PowerMockito.doReturn(Box.builder().items(Arrays.asList(8)).build()).when(service, "getBox", list, 1);

        } catch (Exception e) {
            fail(UNEXPECTED_EXCEPTION + e.getMessage());
        }

        List<Box> result = null;
        try {
            result = service.packageArticle(chainArticleToArticle);
        } catch (BusinessException e) {
            fail(UNEXPECTED_EXCEPTION + e.getMessage());
        }

        List<Box> expected = getBoxList();

        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(expected.size(), result.size());

        for (int i = 0; i < result.size(); i++) {
            assertBox(expected.get(i), result.get(i));
        }

        try {
            PowerMockito.verifyPrivate(service).invoke("validateChainArticle", chainArticleToArticle);
            PowerMockito.verifyPrivate(service).invoke("chainArticleToArticle", chainArticleToArticle);
            PowerMockito.verifyPrivate(service, times(15)).invoke("getBox", Mockito.anyListOf(Box.class), Mockito.anyInt());
        } catch (Exception e) {
            fail(UNEXPECTED_EXCEPTION + e.getMessage());
        }

    }


    /**
     * Case where articles are bad and not pack to boxes
     */
    @Test
    public void shouldPackageArticleFailedWithException() {

        String chainArticleToArticle = StringUtils.EMPTY;

        try {
            PowerMockito.doThrow(new BusinessException((ConstantMessage.CHAIN_ARTICLE_EMPTY))).when(service, "validateChainArticle", chainArticleToArticle);
        } catch (Exception e) {
            fail(UNEXPECTED_EXCEPTION + e.getMessage());
        }

        try {
            service.packageArticle(chainArticleToArticle);
            fail(EXCEPTION_EXPECTED);
        } catch (BusinessException e) {
            assertEquals(ConstantMessage.CHAIN_ARTICLE_EMPTY, e.getMessage());
        }

        try {
            PowerMockito.verifyPrivate(service).invoke("validateChainArticle", chainArticleToArticle);
            PowerMockito.verifyPrivate(service, never()).invoke("chainArticleToArticle", chainArticleToArticle);
            PowerMockito.verifyPrivate(service, never()).invoke("getBox", Mockito.anyListOf(Box.class), Mockito.anyInt());
        } catch (Exception e) {
            fail(UNEXPECTED_EXCEPTION + e.getMessage());
        }

    }

    /**
     * Case where the chain article is transform in a list of article
     */
    @Test
    public void shouldMapChainArticleToArticle() {

        String chainArticleToArticle = "163841689525773";
        List<Integer> result = null;
        try {
            result = Whitebox.invokeMethod(service, "chainArticleToArticle", chainArticleToArticle);
        } catch (Exception e) {
            fail(UNEXPECTED_EXCEPTION + e.getMessage());
        }

        List<Integer> expected = Arrays.asList(9, 8, 8, 7, 7, 6, 6, 5, 5, 4, 3, 3, 2, 1, 1);

        assertTrue(CollectionUtils.isNotEmpty(result));
        assertEquals(expected.size(), result.size());

        for (int i = 0; i < result.size(); i++) {
            assertEquals(expected.get(i), result.get(i));
        }

    }

    /**
     * Case where the chain article is valid
     */
    @Test
    public void shouldValidateChainArticle() {

        String chainArticleToArticle = "163841689525773";
        try {
            Whitebox.invokeMethod(service, "validateChainArticle", chainArticleToArticle);
        } catch (Exception e) {
            fail(UNEXPECTED_EXCEPTION + e.getMessage());
        }

    }

    /**
     * Case where the chain article is empty
     */
    @Test
    public void shouldHaveEmptyExceptionIfChainArticleIsEmpty() {

        String chainArticleToArticle = StringUtils.EMPTY;
        try {
            Whitebox.invokeMethod(service, "validateChainArticle", chainArticleToArticle);
            fail(EXCEPTION_EXPECTED);
        } catch (BusinessException e) {
            assertEquals(ConstantMessage.CHAIN_ARTICLE_EMPTY, e.getMessage());
        } catch (Exception e) {
            fail(UNEXPECTED_EXCEPTION + e.getMessage());
        }

    }

    /**
     * Case where the chain of article has a bad format. The string has a T and not only numbers between 1 and 9
     */
    @Test
    public void shouldHaveBadFormatExceptionIfChainArticleHasT() {

        String chainArticleToArticle = "1T63841689525773";
        try {
            Whitebox.invokeMethod(service, "validateChainArticle", chainArticleToArticle);
        } catch (BusinessException e) {
            assertEquals(ConstantMessage.CHAIN_ARTICLE_BAD_FORMAT, e.getMessage());
        } catch (Exception e) {
            fail(UNEXPECTED_EXCEPTION + e.getMessage());
        }

    }

    /**
     * Case of an article with a size of 6 and a box with a remaining capacity of 4 which can accept the article
     */
    @Test
    public void shouldGetBoxWithItem6AndRemainingCapacity4() {

        List<Box> boxList = getBoxList();
        Integer item = 3;
        Box result = null;
        try {
            result = Whitebox.invokeMethod(service, "getBox", boxList, item);
        } catch (Exception e) {
            fail(UNEXPECTED_EXCEPTION + e.getMessage());
        }

        Box expected = Box.builder().items(Arrays.asList(6)).build();

        assertNotNull(result);
        assertTrue(CollectionUtils.isNotEmpty(result.getItems()));
        assertBox(expected, result);

    }

    /**
     * Case of an article with a size of 7 and no box to accept it. We should get a new box.
     */
    @Test
    public void shouldGetNewBox() {

        List<Box> boxList = getBoxList();
        Integer item = 7;
        Box result = null;
        try {
            result = Whitebox.invokeMethod(service, "getBox", boxList, item);
        } catch (Exception e) {
            fail(UNEXPECTED_EXCEPTION + e.getMessage());
        }

        assertNotNull(result);
        assertTrue(CollectionUtils.isEmpty(result.getItems()));
        assertEquals(Constants.BOX_CAPACITY, result.getRemainingCapacity());

    }

    /**
     * Utility method to prepare a list of boxes
     * @return list of boxes
     */
    private List<Box> getBoxList() {
        Box box1 = Box.builder().items(Arrays.asList(9, 1)).build();
        Box box2 = Box.builder().items(Arrays.asList(8, 2)).build();
        Box box3 = Box.builder().items(Arrays.asList(8, 1)).build();
        Box box4 = Box.builder().items(Arrays.asList(7, 3)).build();
        Box box5 = Box.builder().items(Arrays.asList(7, 3)).build();
        Box box6 = Box.builder().items(Arrays.asList(6, 4)).build();
        Box box7 = Box.builder().items(Arrays.asList(6)).build();
        Box box8 = Box.builder().items(Arrays.asList(5, 5)).build();

        return Arrays.asList(box1, box2, box3, box4, box5, box6, box7, box8);
    }

    /**
     * Method to verify the state of a box
     * @param expected box expected
     * @param result box got after the test
     */
    private void assertBox(Box expected, Box result) {
        assertEquals(expected.getItems().size(), result.getItems().size());
        assertEquals(expected.getRemainingCapacity(), result.getRemainingCapacity());
    }
}
