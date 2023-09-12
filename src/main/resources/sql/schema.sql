CREATE TABLE IF NOT EXISTS member -- 회원 목록
(	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, -- 회원 고유 번호(PK)
    login_id VARCHAR(20) NOT NULL UNIQUE, -- 로그인 아이디
	pw VARCHAR(20) NOT NULL, -- 비밀번호
	email VARCHAR(20) NOT NULL UNIQUE, -- 이메일
	name VARCHAR(20) NOT NULL, -- 이름
	phone_number VARCHAR(13) NOT NULL, -- 전화번호
	id_card_number CHAR(13) NOT NULL UNIQUE, -- 주민등록번호(생성시 임의 입력 예정)
	address TEXT NOT NULL, -- 주소
	gender CHAR(1) NOT NULL CHECK (gender IN ('M', 'F')), -- 성별 (M : 남성, F : 여성)
	authority CHAR(1) DEFAULT 'A' CHECK (authority IN ('M', 'A', 'B')), -- 회원 상태 (M : 관리자,  A : 활성 유저, B : 차단된 유저)
	date_created DATE DEFAULT (CURRENT_DATE) -- 회원가입 날짜
);


CREATE TABLE IF NOT EXISTS product -- 상품 목록
(	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, -- 상품 고유 번호(PK)
	status CHAR(1) DEFAULT 'T' CHECK (status IN ('T', 'F')),-- 상품 판매 가능 상태
	name VARCHAR(30) NOT NULL, -- 이름
    stock SMALLINT NOT NULL DEFAULT 0, -- 재고
    type_region VARCHAR(20), -- 지역
    type_kind VARCHAR(10), -- 주종
    creator VARCHAR(20), -- 제조사
    alcohol FLOAT, -- 도수
	volume INT UNSIGNED, -- ML
	price INT UNSIGNED NOT NULL, -- 상품 가격
    img_thumb TEXT, -- 상품 썸네일 이미지 파일 경로
    img_exp1 TEXT, -- 상품 상세 내용 이미지 파일 경로 (기존: img_context -변경-> img_exp1~2)
    img_exp2 TEXT
);

CREATE TABLE IF NOT EXISTS inquiry -- 문의 목록
(	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, -- 문의 고유 번호(PK)
	type CHAR(1) NOT NULL CHECK(type IN ('P', 'D')), -- 문의 종류 (P: 상품문의, D: 배송문의)
    product_id BIGINT UNSIGNED, -- 문의 상품 고유번호
	inquirer_id BIGINT UNSIGNED NOT NULL, -- 문의 회원의 회원 고유 번호(FK)
    title VARCHAR(50) NOT NULL, -- 문의 제목
    content TEXT NOT NULL, -- 문의 내용
    answer TEXT, -- 답변 내용
    date_inquired DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 문의 날짜 및 시간
    date_replied DATETIME ON UPDATE CURRENT_TIMESTAMP, -- 답변 날짜 및 시간
    FOREIGN KEY(inquirer_id) REFERENCES member(id),	-- 외래키 지정
    FOREIGN KEY(product_id) REFERENCES product(id) -- 외래키 지정
);

CREATE TABLE IF NOT EXISTS purchase -- 통합 주문 목록
(	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, -- 통합 주문 고유 번호(PK)
	member_id BIGINT UNSIGNED NOT NULL, -- 주문자의 회원 고유번호(FK)
    total_price INT UNSIGNED NOT NULL, -- 총 구입 가격
    status CHAR(8) NOT NULL DEFAULT "구매완료", -- 구매 상태 (상황에 따라 배송 추적으로 변경 가능)
    date_created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 주문 날짜 및 시간
    FOREIGN KEY(member_id) REFERENCES member(id) -- 외래키 지정
);

CREATE TABLE IF NOT EXISTS purchase_detail -- 주문 상세 내역
(	purchase_id BIGINT UNSIGNED NOT NULL, -- 통합 주문 고유 번호
	member_id BIGINT UNSIGNED NOT NULL, -- 주문자의 고유 번호
    product_id BIGINT UNSIGNED NOT NULL, -- 주문 상품별 고유 번호
    price INT UNSIGNED NOT NULL, -- 주문 상품의 개당 가격
    quantity SMALLINT UNSIGNED NOT NULL, -- 주문 상품의 수량
    FOREIGN KEY(purchase_id) REFERENCES purchase(id), -- 외래키 지정 시작
    FOREIGN KEY(member_id) REFERENCES member(id),
    FOREIGN KEY(product_id) REFERENCES product(id) -- 외래키 지정 종료
);

CREATE TABLE IF NOT EXISTS cart -- 장바구니 목록
(	member_id BIGINT UNSIGNED NOT NULL, -- 회원 고유 번호
	product_id BIGINT UNSIGNED NOT NULL, -- 상품 고유 번호
    quantity SMALLINT UNSIGNED NOT NULL, -- 장바구니 내 해당 상품 수량
    FOREIGN KEY(member_id) REFERENCES member(id), -- 외래키 지정 시작
    FOREIGN KEY(product_id) REFERENCES product(id) -- 외래키 지정 종료
);

CREATE TABLE IF NOT EXISTS notice -- 공지사항
(   id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, -- 공지사항 고유 번호(PK)
	date_created DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 게시일
    date_updated DATETIME ON UPDATE CURRENT_TIMESTAMP, -- 수정일
	title VARCHAR(50) NOT NULL, -- 공지사항 제목
    content TEXT NOT NULL -- 문의 내용
);

CREATE TABLE IF NOT EXISTS product_review -- 상품리뷰
(	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, -- 상품리뷰 고유 번호(PK)
	member_id BIGINT UNSIGNED NOT NULL,				-- 회원 고유 번호
	product_id BIGINT UNSIGNED NOT NULL,			-- 상품 고유 번호
	review_satisfaction VARCHAR(10) NOT NULL CHECK (review_satisfaction IN('VH','H','M','L','VL')),
    -- 리뷰 만족도 ('VH' : 아주만족(별 5개) , 'H' : 만족(별 4개) , 'M' : 보통(별 3개), 'L' : 불만족(별 2개), 'VL' : 아주불만족(별 1개)
    content TEXT NOT NULL, 	-- 리뷰 후기
    date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 주문 날짜 및 시간
    img_exp1 TEXT,			-- 사용자 등록 이미지
    img_exp2 TEXT,
    img_exp3 TEXT,
    FOREIGN KEY(member_id) REFERENCES member(id), -- 외래키 지정 member id
    FOREIGN KEY(product_id) REFERENCES product(id) -- 외래키 지정 product id
);