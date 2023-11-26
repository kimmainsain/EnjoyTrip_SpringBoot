use enjoytrip;

# info랑 description 합쳐서 조회
select info.content_id, info.title, info.content_type_id, info.addr1, info.addr2, info.zipcode, info.tel, info.first_image,
       info.sido_code, info.gugun_code, info.latitude, info.longitude,  des.homepage, des.overview, des.telname
from attraction_info as info
         join attraction_description as des
              on info.content_id = des.content_id;

# attraction view
create view attraction as
select info.content_id as content_id, info.title as title, info.content_type_id as content_type_id, info.addr1 as addr1, info.addr2 as addr2, info.zipcode as zipcode, info.tel as tel, info.first_image as first_image,
       info.sido_code as sido_code, info.gugun_code as gugun_code, info.latitude as latitude, info.longitude as longitude,  des.homepage as homepage, des.overview as overview, des.telname as telname
from attraction_info as info
         join attraction_description as des
              on info.content_id = des.content_id;

# test code
select * from attraction where content_id > 0 limit 10;


create table real_user(
                          no int auto_increment primary key,
                          id varchar(20) not null unique ,
                          pw char(64) not null,
                          nickname varchar(20) not null unique,
                          email varchar(100) not null,
                          salt char(12) not null,
                          enabled bool not null default true
);

create table admin(
                      no int primary key,
                      flag bool not null,
                      foreign key(no) references real_user(no) on delete cascade
);

create view user as
select r.no as no, r.id as id, r.pw as pw, r.nickname as nickname, r.email as email, r.salt as salt, admin.flag as admin, r.enabled as enabled
from real_user as r
left join admin
on r.no = admin.no;


# 최초 관리자 ssafy 계정 생성
insert into real_user (id, pw, nickname, email, salt)
select 'ssafy', 'd9717fc3eb664fe59e6362db7a624bf62d9362d619615541b07faf5736f2e004', '관리자', 'ssafy@ssafy.com', '4hXouqZA150=' from dual
where not exists
    (
        select id from real_user where id = 'ssafy'
    );
insert into admin (no, flag)
values ((select no from real_user where id = 'ssafy' limit 1), true);


# article_no : 글번호, author_no : 작성자(->user no), title : 글 제목, content : 내용
create table board_free(
                           article_no int auto_increment primary key,
                           author_no int not null,
                           title varchar(100) not null,
                           content text,
                           time datetime default now(),
                           enabled bool not null default true,
                           foreign key(author_no) references real_user(no)
);


create table board_notice(
                             article_no int auto_increment primary key,
                             author_no int not null,
                             title varchar(100) not null,
                             content text,
                             time datetime default now(),
                             enabled bool not null default true,
                             foreign key(author_no) references real_user(no)
);

# 이 테이블에 있는건 관리자 권한 필요 -> 공지사항 같이 관리자 권한 필요한 건 여기 넣어 두자...
# 서블릿에서 board.jsp로 데이터 넘기기 전에 admin 권한 필요한 지 같이 넘겨서
# admin 권한 필요하면 세션에 관리자 있는 사람한테만 글쓰기 버튼이나 수정 버튼 나오게...
create table admin_require(
	name varchar(100) primary key
);

insert into admin_require values ('notice');

create view board_free_view as
select board.article_no as article_no, board.author_no as author_no, board.title as title, board.content as content, board.time as time,
		board.enabled as enabled, u.id as id, u.nickname as nickname
from board_free as board
join real_user as u
on board.author_no = u.no;

create view board_notice_view as
select board.article_no as article_no, board.author_no as author_no, board.title as title, board.content as content, board.time as time,
		board.enabled as enabled, u.id as id, u.nickname as nickname
from board_notice as board
join real_user as u
on board.author_no = u.no;