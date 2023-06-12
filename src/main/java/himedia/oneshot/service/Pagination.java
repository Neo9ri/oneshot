package himedia.oneshot.service;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 페이지네이션 버튼 UI 구현에 필요한 값들을 저장하는 클래스입니다.
 * 활용 예시는 아래의 문서를 참고하세요.
 * @see himedia.oneshot.controller.AdminController
 */
@Getter
@NoArgsConstructor
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
    private boolean existPreviousLastPage; // 하단부 첫 버튼의 이전 페이지가 존재 여부
    private boolean existNextFirstPage; // 하단부 마지막 버튼의 이후 페이지가 존재 여부
    private int fromIndex; // 슬라이싱에 사용될 시작 인덱스
    private int toIndex; // 슬라이싱에 사용될 끝 인덱스

    /**
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

        if (lastPage == totalPage)
            existNextFirstPage = false;
        else {
            nextFirstPage = lastPage + 1;
            existNextFirstPage = true;
        }

        fromIndex = rangeItem * (requestPage - 1);
        toIndex = (rangeItem * requestPage);
    }

    /**
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

        if (lastPage == totalPage)
            existNextFirstPage = false;
        else {
            nextFirstPage = lastPage + 1;
            existNextFirstPage = true;
        }

        fromIndex = rangeItem * (requestPage - 1);
        toIndex = (rangeItem * requestPage);
    }
}
