CREATE DATABASE IF NOT EXISTS oneshot;

USE oneshot;

CREATE TABLE IF NOT EXISTS member -- 회원 목록
(	id INT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT, -- 회원 고유 번호(PK)
    login_id VARCHAR(20) NOT NULL UNIQUE, -- 로그인 아이디
	pw VARCHAR(20) NOT NULL, -- 비밀번호
	email VARCHAR(20) NOT NULL UNIQUE, -- 이메일
	name VARCHAR(20) NOT NULL, -- 이름
	phone_number VARCHAR(13) NOT NULL, -- 전화번호
	id_card_number CHAR(13) NOT NULL UNIQUE, -- 주민등록번호(생성시 임의 입력 예정)
	address TEXT NOT NULL, -- 주소
	gender CHAR(1) NOT NULL CHECK (gender IN ('M', 'F')), -- 성별 (M : 남성, F : 여성)
	authority CHAR(1) NOT NULL DEFAULT 'A' CHECK (authority IN ('M', 'A', 'B')), -- 회원 상태 (M : 관리자,  A : 활성 유저, B : 차단된 유저)
	date_created DATE DEFAULT (CURRENT_DATE) -- 회원가입 날짜 
);

CREATE TABLE IF NOT EXISTS inquiry -- 문의 목록
(	id INT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT, -- 문의 고유 번호(PK)
	type CHAR(1) NOT NULL CHECK(type IN ('P', 'D')), -- 문의 종류 (P: 상품문의, D: 배송문의)
	inquirer_id INT UNSIGNED NOT NULL, -- 문의 회원의 회원 고유 번호(FK)
    content TEXT NOT NULL, -- 문의 내용
    date_created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 문의 날짜 및 시간
    FOREIGN KEY(inquirer_id) REFERENCES member(id)	-- 외래키 지정
);

CREATE TABLE IF NOT EXISTS product -- 상품 목록
(	id INT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT, -- 상품 고유 번호(PK)
	name VARCHAR(30) NOT NULL, -- 이름
    type_local VARCHAR(20), -- 지역
    type_kind VARCHAR(10), -- 주종
    creator VARCHAR(20), -- 제조사
	price SMALLINT UNSIGNED NOT NULL, -- 상품 가격
    img_thum TEXT, -- 상품 썸네일 이미지 파일 경로
    img_content TEXT -- 상품 상세 내용 이미지 파일 경로
);

CREATE TABLE IF NOT EXISTS purchase -- 통합 주문 목록
(	id BIGINT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT, -- 통합 주문 고유 번호(PK)
	member_id INT UNSIGNED NOT NULL, -- 주문자의 회원 고유번호(FK)
    total_price INT UNSIGNED NOT NULL, -- 총 구입 내역
    status CHAR(8) NOT NULL DEFAULT "구매완료", -- 구매 상태 (상황에 따라 배송 추적으로 변경 가능)
    date_created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 주문 날짜 및 시간
    FOREIGN KEY(member_id) REFERENCES member(id) -- 외래키 지정
);

CREATE TABLE IF NOT EXISTS purchase_detail -- 주문 상세 내역
(	purchase_id BIGINT UNSIGNED NOT NULL, -- 통합 주문 고유 번호
	member_id INT UNSIGNED NOT NULL, -- 주문자의 고유 번호
    product_id INT UNSIGNED NOT NULL, -- 주문 상품별 고유 번호
    price INT UNSIGNED NOT NULL, -- 주문 상품의 개당 가격
    qauntity SMALLINT UNSIGNED NOT NULL, -- 주문 상품의 수량
    FOREIGN KEY(purchase_id) REFERENCES purchase(id), -- 외래키 지정 시작
    FOREIGN KEY(member_id) REFERENCES member(id),
    FOREIGN KEY(product_id) REFERENCES product(id) -- 외래키 지정 종료
);

CREATE TABLE IF NOT EXISTS cart -- 장바구니 목록
(	member_id INT UNSIGNED NOT NULL, -- 회원 고유 번호
	product_id INT UNSIGNED NOT NULL, -- 상품 고유 번호
    quantity SMALLINT UNSIGNED NOT NULL, -- 해당 상품 수량
    FOREIGN KEY(member_id) REFERENCES member(id), -- 외래키 지정 시작
    FOREIGN KEY(product_id) REFERENCES product(id) -- 외래키 지정 종료
);


DROP TABLE IF EXISTS cart, purchase_detail, purchase, product, inquiry, member -- 테이블 전체 삭제