CREATE DATABASE IF NOT EXISTS oneshot;

USE oneshot;

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
	name VARCHAR(30) NOT NULL, -- 이름
    quantity SMALLINT UNSIGNED NOT NULL,
    type_local VARCHAR(20), -- 지역
    type_kind VARCHAR(10), -- 주종
    creator VARCHAR(20), -- 제조사
    alcohol FLOAT, -- 도수
	price INT UNSIGNED NOT NULL, -- 상품 가격
    img_thumb TEXT, -- 상품 썸네일 이미지 파일 경로
    img_exp1 TEXT, -- 상품 상세 내용 이미지 파일 경로 (기존: img_context -변경-> img_exp1~3)
    img_exp2 TEXT,
    img_exp3 TEXT
);

CREATE TABLE IF NOT EXISTS inquiry -- 문의 목록
(	id BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT, -- 문의 고유 번호(PK)
	type CHAR(1) NOT NULL CHECK(type IN ('P', 'D')), -- 문의 종류 (P: 상품문의, D: 배송문의)
    product_id BIGINT UNSIGNED, -- 문의 상품 고유번호
	inquirer_id BIGINT UNSIGNED NOT NULL, -- 문의 회원의 회원 고유 번호(FK)
    title VARCHAR(50) NOT NULL, -- 문의 제목
    content TEXT NOT NULL, -- 문의 내용
    answer TEXT, -- 답변 내용
    date_inquired DATETIME DEFAULT CURRENT_TIMESTAMP, -- 문의 날짜 및 시간
    date_replied DATETIME, -- 답변 날짜 및 시간
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



INSERT INTO member -- 관리자, 유저 생성
(login_id, pw, email, name, phone_number, id_card_number, address, gender, authority)
values
('admin', 'admin1234', 'admin@abc.com', '관리자', '01000000000', '0000000000000', '서울특별시 서대문구 창천동 18-29 7층', 'M', 'M'), -- 관리자
('member01', 'member1234', 'member01@def.com', '홍길동', '01011111111', '8001011234567', '서울특별시 서대문구 창천동 버티고타워 7층', 'M', 'A'),
('member02', 'member1234', 'member02@cde.com', '이미자', '01022222222', '7001012345678', '서울특별시 서대문구 창천동 버티고타워 8층', 'F', 'A'),
('block', 'member1234', 'block@block.com', '사기꾼', '01100000000', '6006061234567', '경상북도 청송군 진보면 양정길 231', 'M', 'B'); -- 차단된 회원

INSERT INTO product
(name, quantity,type_local, type_kind, creator, alcohol, price, img_thumb, img_exp1, img_exp2, img_exp3)
values
('운암', 1,'전북, 전남, 경북, 경남', '증류주', '맑은 내일', 32, 12900, 'img/product/thumbnail/운암.jpg', 'img/product/explanation/운암_exp01.jpg', 'img/product/explanation/운암_exp02.jpg', NULL),
('용25', 1,'강원, 세종권', '증류주', '두루양조', 25, 12000, 'img/product/thumbnail/용25.jpg', 'img/product/explanation/용25_exp01.jpg', 'img/product/explanation/용25_exp02.jpg', NULL),
('월고해', 1,'전북, 전남, 경북, 경남', '증류주', '인산 농장', 42, 104500, 'img/product/thumbnail/월고해.jpg', 'img/product/explanation/월고해_exp01.jpg', 'img/product/explanation/월고해_exp02.jpg', NULL),
('항아리숙성 주향이오', 1,'전북, 전남, 경북, 경남', '숙성 전통주', '담을술공방', 25, 15300, 'img/product/thumbnail/항아리숙성_주향이오.jpg', 'img/product/explanation/항아리숙성_주향이오.jpg', NULL, NULL),
('천사의 선물', 1,'전북, 전남, 경북, 경남', '과실주', '내변산', 17, 9500, 'img/product/thumbnail/천사의_선물.jpg', 'img/product/explanation/천사의_선물_exp01.jpg', 'img/product/explanation/천사의_선물_exp02.jpg', NULL),
('복분자 와인', 1,'전북, 전남, 경북, 경남', '과실주', '참주가', 11, 2850, 'img/product/thumbnail/복분자_와인.jpg',  'img/product/explanation/복분자_와인_exp01.jpg', 'img/product/explanation/복분자_와인_exp02.jpg', NULL),
('화이트 와인 스위트', 1,'전북, 전남, 경북, 경남', '과실주', '수도산와이너리', 11.5, 25650, 'img/product/thumbnail/화이트_와인_스위트.jpg', 'img/product/explanation/화이트_와인_스위트_exp01.jpg', 'img/product/explanation/화이트_와인_스위트_exp02.jpg', NULL),
('복숭아 스파클링 와인', 1,'강원, 세종권', '과실주', '솔티마을', 9, 14250, 'img/product/thumbnail/복숭아_스파클링_와인.jpg', 'img/product/explanation/복숭아_스파클링_와인_exp01.jpg', 'img/product/explanation/복숭아_스파클링_와인_exp02.jpg', NULL),
('산사춘', 1,'서울, 경기, 인천권', '약주/청주', '배상면주가', 12, 4300, 'img/product/thumbnail/산사춘.jpg', 'img/product/explanation/산사춘_exp01.jpg', NULL, NULL),
('대나무술', 1,'전북, 전남, 경북, 경남', '약주/청주', '백운주가', 11, 3800, 'img/product/thumbnail/대나무술.jpg', 'img/product/explanation/대나무술_exp01.jpg', NULL, NULL),
('우담청주', 1,'전북, 전남, 경북, 경남', '약주/청주', '참주가', 13, 3990, 'img/product/thumbnail/우담청주.jpg', 'img/product/explanation/우담청주_exp01.jpg', 'img/product/explanation/우담청주_exp02.jpg', NULL),
('초가 한청', 1,'강원, 세종권', '약주/청주', '초가', 15, 19000, 'img/product/thumbnail/초가_한청.jpg', 'img/product/explanation/초가_한청_exp01.jpg', 'img/product/explanation/초가_한청_exp02.jpg', NULL);

select * from product where id=1;

SELECT * FROM product;
SELECT * FROM cart;
SELECT * FROM member;
SELECT * FROM purchase;
SELECT * FROM purchase_detail;

insert into cart(member_id,product_id,quantity) values(2,1,1);

select quantity from cart where product_id = 1;

update cart set quantity = 1 where product_id = 1;

DROP TABLE IF EXISTS cart, purchase_detail, purchase, product, inquiry, member;
