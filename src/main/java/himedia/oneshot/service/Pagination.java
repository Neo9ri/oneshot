package himedia.oneshot.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

/**
 * 페이지네이션 버튼 UI 구현에 필요한 값들을 저장하는 클래스입니다.
 * 활용 예시는 아래의 문서를 참고하세요.
 * @see himedia.oneshot.controller.AdminController
 */
@Service
@Getter
@NoArgsConstructor
@Slf4j
public class Pagination {
    private int totalItem; // 목록을 구성하는 전체 아이템의 개수
    private int rangeItem; // 한 화면에 보여줄 목록의 최대 개수
    private int button = 5; // 한 화면에 보여줄 버튼의 최대개수
    private int totalPage; // 전체 페이지 수
    private int requestPage; // 요청할 페이지
    private int previousLastPage; // 첫 버튼의 이전 페이지 번호
    private int firstPage; // 첫 버튼의 페이지 번호
    private int lastPage; // 마지막 버튼의 페이지 번호,
    private int nextFirstPage; // 마지막 버튼의 다음 페이지 번호
    private boolean existPreviousLastPage; // 하단부 첫 버튼의 이전 페이지 존재 여부
    private boolean existNextFirstPage; // 하단부 마지막 버튼의 이후 페이지 존재 여부
    private int fromIndex; // 슬라이싱에 사용될 시작 인덱스
    private int toIndex; // 슬라이싱에 사용될 끝 인덱스

    /**
     * @deprecated
     * 하단의 페이지 버튼이 최대 5개인 페이지네이션 UI를 구현하는 Pagination 생성자입니다.
     * @param totalItem 전체 목록 개수
     * @param rangeItem 한 페이지 내의 목록 개수
     * @param requestPage 요청 페이지
     */
    public Pagination(int totalItem, int rangeItem, int requestPage) {
        this.rangeItem = rangeItem;
        this.requestPage = requestPage;
        this.totalItem = totalItem;

        totalPage = (int)Math.ceil(totalItem/(float)rangeItem);
        if (requestPage>totalPage)
            requestPage=totalPage;
        int lastGroup = (totalPage-1)/ button;
        int currentGroup = (requestPage-1)/ button;
        firstPage = currentGroup * button + 1;

        if (currentGroup != lastGroup)
            lastPage = firstPage + button -1;
        else {
            lastPage = totalPage;
        }

        if (firstPage == 1)
            existPreviousLastPage = false;
        else {
            previousLastPage = firstPage -1;
            existPreviousLastPage = true;
        }

        if (lastPage == totalPage) {
            existNextFirstPage = false;
            if(lastPage == 0)
                lastPage = 1;
        } else {
            nextFirstPage = lastPage + 1;
            existNextFirstPage = true;
        }

        fromIndex = rangeItem * (requestPage - 1);
        toIndex = (rangeItem * requestPage);
    }

    /**
     * @deprecated
     * 하단의 페이지 버튼을 설정한 수에 따라 페이지네이션 UI를 구현하는 Pagination 생성자입니다.
     * @param totalItem 전체 목록 개수
     * @param rangeItem 한 페이지 내의 목록 개수
     * @param requestPage 요청 페이지
     * @param button 하단 페이지 버튼의 개수
     */
    public Pagination(int totalItem, int rangeItem, int requestPage, int button) {
        this.totalItem = totalItem;
        this.rangeItem = rangeItem;
        this.requestPage = requestPage;
        this.button = button;

        totalPage = (int)Math.ceil(totalItem/(float)rangeItem);
        if (requestPage>totalPage)
            requestPage=totalPage;
        int lastGroup = (totalPage-1)/ button;
        int currentGroup = (requestPage-1)/ button;
        firstPage = currentGroup * button + 1;

        if (currentGroup != lastGroup)
            lastPage = firstPage + button -1;
        else {
            lastPage = totalPage;
        }

        if (firstPage == 1)
            existPreviousLastPage = false;
        else {
            previousLastPage = firstPage -1;
            existPreviousLastPage = true;
        }

        if (lastPage == totalPage) {
            existNextFirstPage = false;
            if(lastPage == 0)
                lastPage = 1;
        } else {
            nextFirstPage = lastPage + 1;
            existNextFirstPage = true;
        }

        fromIndex = rangeItem * (requestPage - 1);
        toIndex = (rangeItem * requestPage);
    }

    /**
     * View에서 페이징 처리를 한 뒤 해당 값을 모델로 지정합니다.
     * 화면에 생성되는 페이지 버튼의 최대 수는 5개입니다.
     * @param itemList 목록으로 보여줄 타입의 리스트
     * @param itemListName 목록으로 보여줄 타입의 리스트를 View에서 접근하기 위해서 사용할 이름
     *                     (ex) ${itemListName.변수}
     * @param rangeItem 한 페이지에 보여줄 목록의 최대 개수
     * @param requestPage 요청 페이지 번호
     * @param pageBtnName 페이지 버튼의 이름
     *                    (ex) ${pageBtnName.변수}
     * @see himedia.oneshot.controller.AdminController
     */
    public <T> void makePagination(Model model,
                                   List<T> itemList,
                                   String itemListName,
                                   int rangeItem,
                                   Integer requestPage,
                                   String pageBtnName){
        // 한 페이지에 보여줄 목록 개수 설정
        this.rangeItem = rangeItem;
        // 사용자가 요청한 페이지 번호
        try {
            this.requestPage = requestPage;
        } catch (NullPointerException npe){ // 사용자가 요청한 페이지가 없는 경우
            this.requestPage = 1; // 요청 페이지를 첫 페이지로 설정
            System.out.println(this.requestPage);
        }

        totalItem = itemList.size(); // 페이징이 필요한 데이터의 전체 개수
        if (totalItem!=0) {
            totalPage = (int)Math.ceil(totalItem/(float)rangeItem); // 전체 페이지 수
        } else {
            totalPage=1;
        }
        if (this.requestPage>totalPage) // 요청 페이지가 전체 페이지보다 많은 경우
            this.requestPage=totalPage; // 요청 페이지는 마지막 페이지로 설정

        // 페이지 버튼 구성을 위한 설정
        int lastGroup = (totalPage-1)/ button;
        int currentGroup = (this.requestPage-1)/ button;
        firstPage = currentGroup * button + 1;

        if (currentGroup != lastGroup)
            lastPage = firstPage + button -1;
        else {
            lastPage = totalPage;
        }

        if (firstPage == 1)
            existPreviousLastPage = false;
        else {
            previousLastPage = firstPage -1;
            existPreviousLastPage = true;
        }

        if (lastPage == totalPage) {
            existNextFirstPage = false;
            if(lastPage == 0)
                lastPage = 1;
        } else {
            nextFirstPage = lastPage + 1;
            existNextFirstPage = true;
        }

        // 필요한 목록만 슬라이싱하기 위한 인덱스 범위 설정
        fromIndex = rangeItem * (this.requestPage - 1);
        System.out.println("fromIndex : " + fromIndex);
        toIndex = (rangeItem * this.requestPage);

        model.addAttribute(pageBtnName, this);

        try {
            itemList = itemList.subList(fromIndex, toIndex);
        } catch (IndexOutOfBoundsException ioobe) { // 인덱스 범위를 벗어날 경우, 마지막 인덱스 값 재설정
                toIndex = itemList.size();
                itemList = itemList.subList(fromIndex,toIndex);
        } finally {
            model.addAttribute(itemListName, itemList);
        }

    }
}
