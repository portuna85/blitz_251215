-- 기존 posts 테이블에 author_email 컬럼 추가
ALTER TABLE posts ADD COLUMN author_email VARCHAR(255);

-- 기존 데이터가 있다면 author를 author_email에 임시 복사 (나중에 수동으로 정리 필요)
UPDATE posts SET author_email = CONCAT(author, '@temp.com') WHERE author_email IS NULL;

-- NOT NULL 제약 조건 추가
ALTER TABLE posts MODIFY COLUMN author_email VARCHAR(255) NOT NULL;

