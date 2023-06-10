package himedia.oneshot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 페이지네이션 버튼을 구현합니다.
 */
@Getter
@NoArgsConstructor
public class Pagination {
    private int totalItem; // 목록을 구성하는 전체 아이템의 개수
    private int rangeItem; // 한 화면에 보여줄 목록의 최대 개수
    private int rangePage = 5; // 한 화면에 보여줄 버튼의 최대개수
    private int totalPage; // 전체 페이지 수
    private int requestPage; // 요청할 페이지
    private int previousLastPage; // 첫 버튼의 이전 페이지 번호
    private int firstPage; // 첫 버튼의 페이지 번호
    private int lastPage; // 마지막 버튼의 페이지 번호
    private int nextFirstPage; // 마지막 버튼의 다음 페이지 번호
    private int fromIndex; // 쿼리 조회에 사용될 시작 인덱스
    private int toIndex; // 쿼리 조회에 사용될 끝 인덱스

    /**
     *
     * @param rangeItem 한 페이지에서 구현할 목록의 개수
     * @param requestPage 요청 페이지
     * @param totalItem 전체 목록의 개수
     */
    public Pagination(int rangeItem, int requestPage, int totalItem) {
        this.rangeItem = rangeItem;
        this.requestPage = requestPage;
        this.totalItem = totalItem;

        totalPage = (int)Math.ceil(totalItem/(float)rangeItem);
        if (requestPage>totalPage)
            requestPage=totalPage;
        int lastGroup = (totalPage-1)/ rangePage;
        int currentGroup = (requestPage-1)/ rangePage;
        firstPage = currentGroup * rangePage + 1;
        int restPage = requestPage % rangePage;

        if (currentGroup != lastGroup)
            lastPage = firstPage + rangePage -1;
        else {
            lastPage = totalPage;
        }
        previousLastPage = (firstPage == 1)? -1 : firstPage - 1;
        nextFirstPage = (lastPage == totalPage)? -1: lastPage + 1;

        fromIndex = rangeItem * (requestPage - 1);
        toIndex = (rangeItem * requestPage) - 1;
    }
}
