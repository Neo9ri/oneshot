CREATE DATABASE IF NOT EXISTS oneshot;

USE oneshot;

select @@autocommit; -- 자동 커밋 설정 확인
set autocommit = true; -- 자동 커밋 설정

DROP TABLE IF EXISTS notice, product_review, cart, purchase_detail, purchase, product, inquiry, member; -- 테이블 전체 삭제

SELECT * FROM member;
SELECT * FROM product;
SELECT * FROM cart;
SELECT * FROM inquiry;
SELECT * FROM purchase;
SELECT * FROM purchase_detail;
SELECT * FROM notice;

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
    quantity SMALLINT UNSIGNED NOT NULL,
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

INSERT INTO member -- 관리자, 유저 생성
(login_id, pw, email, name, phone_number, id_card_number, address, gender, authority)
values
('admin', 'admin1234', 'admin@abc.com', '관리자', '010-0000-0000', '0000000000000', '서울특별시 서대문구 창천동 18-29 7층', 'M', 'M'), -- 관리자
('member01', 'member1234', 'member01@def.com', '홍길동', '010-1111-1111', '8001011234567', '서울특별시 서대문구 창천동 버티고타워 7층', 'M', 'A'),
('member02', 'member1234', 'member02@cde.com', '이미자', '010-2222-2222', '7001012345678', '서울특별시 서대문구 창천동 버티고타워 8층', 'F', 'A'),
('block', 'member1234', 'block@block.com', '사기꾼', '011-0000-0000', '6006061234567', '경상북도 청송군 진보면 양정길 231', 'M', 'B'); -- 차단된 회원

INSERT INTO member (login_id, pw, email, name, phone_number, id_card_number, address, gender, authority, date_created) -- member 테이블 더미 데이터 100개 생성
SELECT
    CONCAT('user', LPAD(ROW_NUMBER() OVER (ORDER BY (SELECT NULL)), 3, '0')) AS login_id,
    CONCAT('password', LPAD(ROW_NUMBER() OVER (ORDER BY (SELECT NULL)), 3, '0')) AS pw,
    CONCAT('user', LPAD(ROW_NUMBER() OVER (ORDER BY (SELECT NULL)), 3, '0'), '@example.com') AS email,
    CONCAT('User', LPAD(ROW_NUMBER() OVER (ORDER BY (SELECT NULL)), 3, '0')) AS name,
    CONCAT('010-', LPAD(FLOOR(RAND() * 1000), 3, '0'), '-', LPAD(FLOOR(RAND() * 10000), 4, '0')) AS phone_number,
    CONCAT(LPAD(FLOOR(RAND() * 1000000), 6, '0'), '-', LPAD(FLOOR(RAND() * 100000), 5, '0')) AS id_card_number,
    CONCAT('Address', LPAD(ROW_NUMBER() OVER (ORDER BY (SELECT NULL)), 3, '0')) AS address,
    IF(RAND() > 0.5, 'M', 'F') AS gender,
    CASE
        WHEN RAND() < 0.1 THEN 'M'
        WHEN RAND() < 0.3 THEN 'B'
        ELSE 'A'
    END AS authority,
    DATE_SUB(CURRENT_DATE, INTERVAL FLOOR(RAND() * 365) DAY) AS date_created
FROM
    information_schema.tables
LIMIT 100;

INSERT INTO product
(name, quantity, type_region, type_kind, creator, alcohol, volume, price, img_thumb, img_exp1, img_exp2)
values
('대대포',1,'전북, 전남, 경북, 경남','막걸리', '죽향도가',6,600,3600,'img/product/thumbnail/대대포.jpg','img/product/explanation/대대포_exp01.jpg','img/product/explanation/대대포_exp02.jpg'),
('호랑이 생막걸리',1,'서울, 경기, 인천권','막걸리', '배도가',6,750,3300,'img/product/thumbnail/호랑이_생막걸리.jpg','img/product/explanation/호랑이_생막걸리_exp01.jpg','img/product/explanation/호랑이_생막걸리_exp02.jpg'),
('금정산성',1,'전북, 전남, 경북, 경남','막걸리', '(유)금정산성토산주',8,750,3320,'img/product/thumbnail/금정산성.jpg','img/product/explanation/금정산성_exp01.jpg','img/product/explanation/금정산성_exp02.jpg'),
('옛날 생동동주',1,'전북, 전남, 경북, 경남','막걸리', '남도탁주',6,750,2500,'img/product/thumbnail/옛날_생동동주.jpg','img/product/explanation/옛날_생동동주_exp01.jpg','img/product/explanation/옛날_생동동주_exp02.jpg'),
('붉은원숭이',1,'서울, 경기, 인천권','막걸리', '술샘',10.8,375,8550,'img/product/thumbnail/붉은원숭이.jpg','img/product/explanation/붉은원숭이_exp01.jpg','img/product/explanation/붉은원숭이_exp02.jpg'),
('오미자 생막걸리',1,'전북, 전남, 경북, 경남','막걸리', '문경주조',6.5,750,2800,'img/product/thumbnail/오미자_생막걸리.jpg','img/product/explanation/오미자_생막걸리_exp01.jpg','img/product/explanation/오미자_생막걸리_exp02.jpg'),
('강냉이 생막걸리',1,'충북, 충남, 제주도','막걸리', '용두산조은술',6,1000,2560,'img/product/thumbnail/강냉이_생막걸리.jpg','img/product/explanation/강냉이_생막걸리_exp01.jpg','img/product/explanation/강냉이_생막걸리_exp02.jpg'),
('선운산의 아침 복분자 막걸리',1,'전북, 전남, 경북, 경남','막걸리', '국순당고창명주',6,750,4270,'img/product/thumbnail/선운산의_아침_복분자_막걸리.jpg','img/product/explanation/선운산의_아침_복분자_막걸리_exp01.jpg','img/product/explanation/선운산의_아침_복분자_막걸리_exp02.jpg'),
('해창막걸리',1,'전북, 전남, 경북, 경남','막걸리', '해창주조',9,900,10800,'img/product/thumbnail/해창막걸리.jpg','img/product/explanation/해창막걸리_exp01.jpg','img/product/explanation/해창막걸리_exp02.jpg'),
('삼양춘 생막걸리',1,'서울, 경기, 인천권','막걸리', '송도향',12.5,500,12160,'img/product/thumbnail/삼양춘_생막걸리.jpg','img/product/explanation/삼양춘_생막걸리_exp01.jpg','img/product/explanation/삼양춘_생막걸리_exp02.jpg'),
('얼떨결에 민트',1,'강원, 세종권','막걸리', '동강주조',6,935,8550,'img/product/thumbnail/얼떨결에_민트.jpg','img/product/explanation/얼떨결에_민트_exp01.jpg','img/product/explanation/얼떨결에_민트_exp02.jpg'),
('악양생막걸리',1,'전북, 전남, 경북, 경남','막걸리', '악양주조',6,750,3320,'img/product/thumbnail/악양생막걸리.jpg','img/product/explanation/악양생막걸리_exp01.jpg','img/product/explanation/악양생막걸리_exp02.jpg'),
('희양산 생막걸리',1,'전북, 전남, 경북, 경남','막걸리', '두술도가',9,650,7600,'img/product/thumbnail/희양산_생막걸리.jpg','img/product/explanation/희양산_생막걸리_exp01.jpg','img/product/explanation/희양산_생막걸리_exp02.jpg'),
('부자막걸리',1,'서울, 경기, 인천권','막걸리', '배혜정도가',10,375,4900,'img/product/thumbnail/부자막걸리.jpg','img/product/explanation/부자막걸리_exp01.jpg','img/product/explanation/부자막걸리_exp02.jpg'),
('골목막걸리 프리미엄',1,'충북, 충남, 제주도','막걸리', '주로(주)',12,350,8070,'img/product/thumbnail/골목막걸리_프리미엄.jpg','img/product/explanation/골목막걸리_프리미엄_exp01.jpg','img/product/explanation/골목막걸리_프리미엄_exp02.jpg'),
('연꽃담은술',1,'서울, 경기, 인천권','막걸리', '한통술 이노베이션(주)',8,850,12000,'img/product/thumbnail/연꽃담은술.jpg','img/product/explanation/연꽃담은술_exp01.jpg','img/product/explanation/연꽃담은술_exp02.jpg'),
('지란지교',1,'전북, 전남, 경북, 경남','막걸리', '지란지교',12,500,17100,'img/product/thumbnail/지란지교.jpg','img/product/explanation/지란지교_exp01.jpg','img/product/explanation/지란지교_exp02.jpg'),
('종천생막걸리',1,'충북, 충남, 제주도','막걸리', '종천주조',6,750,2370,'img/product/thumbnail/종천생막걸리.jpg','img/product/explanation/종천생막걸리_exp01.jpg','img/product/explanation/종천생막걸리_exp02.jpg'),
('용25', 1,'강원, 세종권', '증류주', '두루양조', 25, 375, 12000, 'img/product/thumbnail/용25.jpg', 'img/product/explanation/용25_exp01.jpg', 'img/product/explanation/용25_exp02.jpg'),
('월고해', 1,'전북, 전남, 경북, 경남', '증류주', '인산 농장', 42, 375, 104500, 'img/product/thumbnail/월고해.jpg', 'img/product/explanation/월고해_exp01.jpg', 'img/product/explanation/월고해_exp02.jpg'),
('항아리숙성 주향이오', 1,'전북, 전남, 경북, 경남', '기타주', '담을술공방', 25, 375, 15300, 'img/product/thumbnail/항아리숙성_주향이오.jpg', 'img/product/explanation/항아리숙성_주향이오.jpg', NULL),
('천사의 선물', 1,'전북, 전남, 경북, 경남', '과실주', '내변산', 17, 375, 9500, 'img/product/thumbnail/천사의_선물.jpg', 'img/product/explanation/천사의_선물_exp01.jpg', 'img/product/explanation/천사의_선물_exp02.jpg'),
('복분자 와인', 1,'전북, 전남, 경북, 경남', '과실주', '참주가', 11, 375, 2850, 'img/product/thumbnail/복분자_와인.jpg',  'img/product/explanation/복분자_와인_exp01.jpg', 'img/product/explanation/복분자_와인_exp02.jpg'),
('화이트 와인 스위트', 1,'전북, 전남, 경북, 경남', '과실주', '수도산와이너리', 11.5, 375, 25650, 'img/product/thumbnail/화이트_와인_스위트.jpg', 'img/product/explanation/화이트_와인_스위트_exp01.jpg', 'img/product/explanation/화이트_와인_스위트_exp02.jpg'),
('복숭아 스파클링 와인', 1,'강원, 세종권', '과실주', '솔티마을', 9, 375, 14250, 'img/product/thumbnail/복숭아_스파클링_와인.jpg', 'img/product/explanation/복숭아_스파클링_와인_exp01.jpg', 'img/product/explanation/복숭아_스파클링_와인_exp02.jpg'),
('산사춘', 1,'서울, 경기, 인천권', '약주/청주', '배상면주가', 12, 375, 4300, 'img/product/thumbnail/산사춘.jpg', 'img/product/explanation/산사춘_exp01.jpg', NULL),
('대나무술', 1,'전북, 전남, 경북, 경남', '약주/청주', '백운주가', 11, 375, 3800, 'img/product/thumbnail/대나무술.jpg', 'img/product/explanation/대나무술_exp01.jpg', NULL),
('우담청주', 1,'전북, 전남, 경북, 경남', '약주/청주', '참주가', 13, 375, 3990, 'img/product/thumbnail/우담청주.jpg', 'img/product/explanation/우담청주_exp01.jpg', 'img/product/explanation/우담청주_exp02.jpg'),
('초가 한청', 1,'강원, 세종권', '약주/청주', '초가', 15, 375, 19000, 'img/product/thumbnail/초가_한청.jpg', 'img/product/explanation/초가_한청_exp01.jpg', 'img/product/explanation/초가_한청_exp02.jpg'),
('빙탄복', 10,'전북, 전남, 경북, 경남', '과실주', '배상면주가', 7, 370, 5300, 'img/product/thumbnail/빙탄복1.jpg', 'img/product/explanation/빙탄복2.jpg', NULL),
('부안참뽕와인', 10,'전북, 전남, 경북, 경남', '과실주', '내변산', 13, 375, 11000, 'img/product/thumbnail/부안참뽕와인1.jpg', 'img/product/explanation/부안참뽕와인2.jpg', 'img/product/explanation/부안참뽕와인3.jpg'),
('피에스 애플 시드르스', 10,'충북, 충남, 제주도', '과실주', '시나브로와이너리', 5, 750, 39000, 'img/product/thumbnail/피에스_애플_시드르스1.jpg', 'img/product/explanation/피에스_애플_시드르스2.jpg', 'img/product/explanation/피에스_애플_시드르스3.jpg'),
('헤베', 10,'전북, 전남, 경북, 경남', '과실주', '애플리즈', 9, 330, 5700, 'img/product/thumbnail/헤베1.jpg', 'img/product/explanation/헤베2.jpg', 'img/product/explanation/헤베3.jpg'),
('크라테 레드와인', 10,'전북, 전남, 경북, 경남', '과실주', '수도산와이너리', 11.5, 750, 79000, 'img/product/thumbnail/크라테_레드와인1.jpg', 'img/product/explanation/크라테_레드와인2.jpg', 'img/product/explanation/크라테_레드와인3.jpg'),
('크라테 자두와인', 10,'전북, 전남, 경북, 경남', '과실주', '수도산와이너리', 8.5, 375, 27000, 'img/product/thumbnail/크라테_자두와인1.jpg', 'img/product/explanation/크라테_자두와인2.jpg', 'img/product/explanation/크라테_자두와인3.jpg'),
('크라테 로제 미디엄 드라이', 10,'전북, 전남, 경북, 경남', '과실주', '수도산와이너리', 11.5, 375, 27000, 'img/product/thumbnail/크라테_로제_미디엄_드라이1.jpg', 'img/product/explanation/크라테_로제_미디엄_드라이2.jpg', 'img/product/explanation/크라테_로제_미디엄_드라이3.jpg'),
('미라토 로제 스파클링와인', 10,'충북, 충남, 제주도', '과실주', '금용농산', 5, 250, 7000, 'img/product/thumbnail/미라토_로제_스파클링와인1.jpg', 'img/product/explanation/미라토_로제_스파클링와인2.jpg', 'img/product/explanation/미라토_로제_스파클링와인3.jpg'),
('미라토 청수 스위트와인', 10,'충북, 충남, 제주도', '과실주', '금용농산', 12, 750, 32000, 'img/product/thumbnail/미라토_청수_스위트와인1.jpg', 'img/product/explanation/미라토_청수_스위트와인2.jpg', 'img/product/explanation/미라토_청수_스위트와인3.jpg'),
('미라토 샤인머스캣와인', 10,'충북, 충남, 제주도', '과실주', '금용농산', 12, 750, 37000, 'img/product/thumbnail/미라토_샤인머스캣와인1.jpg', 'img/product/explanation/미라토_샤인머스캣와인2.jpg', 'img/product/explanation/미라토_샤인머스캣와인3.jpg'),
('옐로우펀치 파인애플&망고', 10,'충북, 충남, 제주도', '과실주', '댄싱사이더', 5.2, 330, 5900, 'img/product/thumbnail/옐로우펀치_파인애플_망고1.jpg', 'img/product/explanation/옐로우펀치_파인애플_망고2.jpg', null),
('샤토미소 청포도 엠버 화이트와인', 10,'충북, 충남, 제주도', '과실주', '도란원', 12, 375, 23000, 'img/product/thumbnail/샤토미소_청포도_엠버_화이트와인1.jpg', 'img/product/explanation/샤토미소_청포도_엠버_화이트와인2.jpg', 'img/product/explanation/샤토미소_청포도_엠버_화이트와인3.jpg'),
('샤토미소 레인보우 스위트와인', 10,'충북, 충남, 제주도', '과실주', '도란원', 12, 375, 23000, 'img/product/thumbnail/샤토미소_레인보우_스위트와인1.jpg', 'img/product/explanation/샤토미소_레인보우_스위트와인2.jpg', 'img/product/explanation/샤토미소_레인보우_스위트와인3.jpg'),
('샤토미소 복숭아 스위트와인', 10,'충북, 충남, 제주도', '과실주', '도란원', 12, 375, 23000, 'img/product/thumbnail/샤토미소_복숭아_스위트와인1.jpg', 'img/product/explanation/샤토미소_복숭아_스위트와인2.jpg', 'img/product/explanation/샤토미소_복숭아_스위트와인3.jpg'),
('사랑할때 사과과실주', 10,'충북, 충남, 제주도', '과실주', '중원양조', 12, 300, 4200, 'img/product/thumbnail/사랑할때_사과과실주1.jpg', 'img/product/explanation/사랑할때_사과과실주2.jpg', null),
('무주구천동 머루와인', 10,'전북, 전남, 경북, 경남', '과실주', '덕유', 12, 750, 27000, 'img/product/thumbnail/무주구천동_머루와인1.jpg', 'img/product/explanation/무주구천동_머루와인2.jpg', 'img/product/explanation/무주구천동_머루와인3.jpg'),
('달1614 스위트', 10,'전북, 전남, 경북, 경남', '과실주', '덕유', 12, 750, 35000, 'img/product/thumbnail/달1614_스위트1.jpg', 'img/product/explanation/달1614_스위트2.jpg', 'img/product/explanation/달1614_스위트3.jpg'),
('캔와인 캠밸 스위트와인', 10,'충북, 충남, 제주도', '과실주', '블루와인컴퍼니', 10, 330, 9900, 'img/product/thumbnail/캔와인_캠벨_스위트와인1.jpg', 'img/product/explanation/캔와인_캠벨_스위트와인2.jpg', 'img/product/explanation/캔와인_캠벨_스위트와인3.jpg'),
('캔와인 애플 스위트와인', 10,'충북, 충남, 제주도', '과실주', '블루와인컴퍼니', 10, 330, 9900, 'img/product/thumbnail/캔와인_애플_스위트와인1.jpg', 'img/product/explanation/캔와인_애플_스위트와인2.jpg', 'img/product/explanation/캔와인_애플_스위트와인3.jpg'),
('운암24도', 10,'전북, 전남, 경북, 경남', '증류주', '맑은 내일', 24, 375, 7900, 'img/product/thumbnail/운암24.jpg', 'img/product/explanation/운암24_exp01.jpg', 'img/product/explanation/운암24_exp02.jpg'),
('운암32도', 10,'전북, 전남, 경북, 경남', '증류주', '맑은 내일', 32, 375, 12900, 'img/product/thumbnail/운암32.jpg', 'img/product/explanation/운암32_exp01.jpg', 'img/product/explanation/운암32_exp02.jpg'),
('용41도', 10,'강원, 세종권', '증류주', '두루양조', 41, 375, 22000, 'img/product/thumbnail/용41.jpg', 'img/product/explanation/용41_exp01.jpg', 'img/product/explanation/용41_exp02.jpg'),
('강릉소주', 10,'강원, 세종권', '증류주', '우리소주조합', 25, 360, 6500, 'img/product/thumbnail/강릉소주.jpg', 'img/product/explanation/강릉소주_exp01.jpg', 'img/product/explanation/강릉소주_exp02.jpg'),
('토끼소주 가넷', 10,'충북, 충남, 제주도', '증류주', '토끼소주', 46, 750, 120000, 'img/product/thumbnail/토끼소주가넷.jpg', 'img/product/explanation/토끼소주가넷_exp01.jpg', 'img/product/explanation/토끼소주가넷_exp02.jpg'),
('아삭17도', 10,'전북, 전남, 경북, 경남', '증류주', '착한농부', 17, 360, 5600, 'img/product/thumbnail/아삭17.jpg', 'img/product/explanation/아삭17_exp01.jpg', 'img/product/explanation/아삭17_exp02.jpg'),
('아삭골드17도', 10,'전북, 전남, 경북, 경남', '증류주', '착한농부', 17, 360, 8500, 'img/product/thumbnail/아삭골드17.jpg', 'img/product/explanation/아삭골드17_exp01.jpg', 'img/product/explanation/아삭골드17_exp02.jpg'),
('경복궁소주20도', 10,'서울, 경기, 인천권', '증류주', '지비지스피리츠', 20, 350, 12250, 'img/product/thumbnail/경복궁소주20.jpg', 'img/product/explanation/경복궁소주20_exp01.jpg', 'img/product/explanation/경복궁소주20_exp02.jpg'),
('경복궁소주40도', 10,'서울, 경기, 인천권', '증류주', '지비지스피리츠', 40, 350, 28400, 'img/product/thumbnail/경복궁소주40.jpg', 'img/product/explanation/경복궁소주40_exp01.jpg', 'img/product/explanation/경복궁소주40_exp02.jpg'),
('우도국화주', 10,'전북, 전남, 경북, 경남', '증류주', '명품안동소주', 16.9, 375, 5700, 'img/product/thumbnail/우도국화주.jpg', 'img/product/explanation/우도국화주_exp01.jpg', 'img/product/explanation/우도국화주_exp02.jpg'),
('막시모파인오크', 10,'전북, 전남, 경북, 경남', '증류주', '착한농부', 40, 200, 22800, 'img/product/thumbnail/막시모파인오크.jpg', 'img/product/explanation/막시모파인오크_exp01.jpg', 'img/product/explanation/막시모파인오크_exp02.jpg'),
('막시모25도', 10,'전북, 전남, 경북, 경남', '증류주', '착한농부', 25, 360, 11400, 'img/product/thumbnail/막시모25.jpg', 'img/product/explanation/막시모25_exp01.jpg', 'img/product/explanation/막시모25_exp02.jpg'),
('남한산성소주21도', 10,'서울, 경기, 인천권', '증류주', '남한산성소주', 21, 375, 12300, 'img/product/thumbnail/남한산성소주21.jpg', 'img/product/explanation/남한산성소주21_exp01.jpg', 'img/product/explanation/남한산성소주21_exp02.jpg'),
('남한산성소주40도', 10,'서울, 경기, 인천권', '증류주', '남한산성소주', 40, 375, 24900, 'img/product/thumbnail/남한산성소주40.jpg', 'img/product/explanation/남한산성소주40_exp01.jpg', 'img/product/explanation/남한산성소주40_exp02.jpg'),
('제주낭만', 10,'충북, 충남, 제주도', '증류주', '제주바당', 40, 500, 47500, 'img/product/thumbnail/제주낭만.jpg', 'img/product/explanation/제주낭만_exp01.jpg', 'img/product/explanation/제주낭만_exp02.jpg'),
('아치23', 10,'충북, 충남, 제주도', '증류주', '양촌감', 23, 375, 12000, 'img/product/thumbnail/아치23.jpg', 'img/product/explanation/아치23_exp01.jpg', 'img/product/explanation/아치23_exp02.jpg'),
('왕율주40도', 10,'충북, 충남, 제주도', '증류주', '사곡양조', 40, 360, 17000, 'img/product/thumbnail/왕율주40.jpg', 'img/product/explanation/왕율주40_exp01.jpg', 'img/product/explanation/왕율주40_exp02.jpg'),
('진안블랙보리40도', 10,'전북, 전남, 경북, 경남', '증류주', '태평주가', 40, 500, 29000, 'img/product/thumbnail/진안블랙보리40.jpg', 'img/product/explanation/진안블랙보리40_exp01.jpg', 'img/product/explanation/진안블랙보리40_exp02.jpg'),
('진안블랙보리18도', 10,'전북, 전남, 경북, 경남', '증류주', '태평주가', 18, 375, 7000, 'img/product/thumbnail/진안블랙보리18.jpg', 'img/product/explanation/진안블랙보리18_exp01.jpg', 'img/product/explanation/진안블랙보리18_exp02.jpg'),
('금설35도', 10,'충북, 충남, 제주도', '증류주', '금산인삼주', 35, 375, 35000, 'img/product/thumbnail/금설35.jpg', 'img/product/explanation/금설35_exp01.jpg', 'img/product/explanation/금설35_exp02.jpg'),
('선비보드카', 10,'충북, 충남, 제주도', '증류주', '토끼소주', 40, 750, 61000, 'img/product/thumbnail/선비보드카.jpg', 'img/product/explanation/선비보드카_exp01.jpg', 'img/product/explanation/선비보드카_exp02.jpg'),
('퍼플진', 10,'서울, 경기, 인천권', '증류주', '술샘', 36.5, 500, 39000, 'img/product/thumbnail/퍼플진.jpg', 'img/product/explanation/퍼플진_exp01.jpg', 'img/product/explanation/퍼플진_exp02.jpg'),
('민속주안동소주', 10,'전북, 전남, 경북, 경남', '증류주', '민속주안동소주', 45, 400, 29000, 'img/product/thumbnail/민속주안동소주.jpg', 'img/product/explanation/민속주안동소주_exp01.jpg', 'img/product/explanation/민속주안동소주_exp02.jpg'),
('토끼소주골드', 10,'충북, 충남, 제주도', '증류주', '토끼소주', 45, 375, 55000, 'img/product/thumbnail/토끼소주골드.jpg', 'img/product/explanation/토끼소주골드_exp01.jpg', 'img/product/explanation/토끼소주골드_exp02.jpg'),
('백제소주', 10,'전북, 전남, 경북, 경남', '증류주', '내변산', 25, 375, 19000, 'img/product/thumbnail/백제소주.jpg', 'img/product/explanation/백제소주_exp01.jpg', 'img/product/explanation/백제소주_exp02.jpg'),
('짝꿍막걸리', 10,'서울, 경기, 인천권', '막걸리', '한강주조', 8, 620, 4950, 'img/product/thumbnail/짝꿍막걸리.jpg', 'img/product/explanation/짝꿍막걸리_exp01.jpg', 'img/product/explanation/짝꿍막걸리_exp02.jpg');

INSERT INTO inquiry
(type, product_id, inquirer_id, title, content)
values
('P', 11, 2, '상품 문의 드립니다.','구매 개수 제한이 있는지 궁금합니다.'),
('D', 7, 2, '배송 문의 드립니다.','상품 손상없도록 배송 잘 부탁 드립니다.'),
('P', 5, 3, '상품이 궁금합니다.','인기가 많은 제품인가요?.'),
('P', 2, 3, '상품이 궁금합니다.','인기가 많은 제품인가요?.'),
('D', 12, 2, '배송 문의 드립니다.','상품 손상없도록 배송 잘 부탁 드립니다.'),
('D', 8, 3, '배송은 퀵으로 받을 수 있나요?.','빨리받고 싶은데 퀵으로 받을 수 있나요?.');

INSERT INTO NOTICE
(title, content)
values
('7월 휴무 공지(여름휴가)', '7월 26일부터 28일은 원샷팀 여름휴가 기간으로 배송,문의 업무가 중단됩니다.'),
('배송관련 안내드립니다.','주문 폭주로 배송이 지연되고 있습니다. ');

insert into cart(member_id,product_id,quantity) values(2,4,1);
insert into cart(member_id,product_id,quantity) values(2,5,1);
insert into cart(member_id,product_id,quantity) values(2,6,1);
insert into cart(member_id,product_id,quantity) values(3,4,1);

select quantity from cart where product_id = 1;

update cart set quantity = 1 where product_id = 1;


truncate table cart;
truncate table purchase_detail;
delete from cart where member_id = 2;
select product_id, quantity from cart where member_id = 2;
select * from purchase where member_id = 2;
select * from purchase_detail where purchase_id = 2;
SELECT pd.*, p.name FROM purchase_detail pd INNER JOIN product p ON pd.product_id = p.id WHERE pd.purchase_id = 2;
update product set status='F' where id=1;
select * from product where name like '%암' and status like 'T';
SELECT pd.*, p.name
FROM purchase_detail pd
JOIN product p ON pd.product_id = p.id;

SELECT p.date_created
FROM purchase p
JOIN purchase_detail pd ON p.id = pd.purchase_id
WHERE pd.product_id = 31 AND pd.member_id = 2;

SELECT p.id
FROM purchase p
JOIN purchase_detail pd on p.id  = pd.purchase_id
WHERE pd.product_id= 31 AND pd.member_id = 2;

truncate table product_review;

SELECT pr.*, p.date_created, m.name 
FROM product_review pr 
JOIN purchase_detail pd ON pr.member_id = pd.member_id AND pr.product_id = pd.product_id 
JOIN purchase p ON pd.purchase_id = p.id 
JOIN member m ON pr.member_id = m.id 
WHERE pr.product_id = 25;

select p.date_created from purchase p 
join purchase_detail pd on p.id = pd.purchase_id 
where pd.product_id =25 and pd.member_id = 2;

SELECT * FROM purchase;
SELECT * FROM purchase_detail;
select * from product_review;