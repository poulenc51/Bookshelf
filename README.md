# Bookshelf
書籍管理システム

## 使用方法

このプロジェクトを使用するには、以下の手順に従ってください：

1. リポジトリをクローンする：
   ```bash
   git clone https://github.com/poulenc51/Bookshelf.git

2. プロジェクトディレクトリに移動する：
    ```bash
   cd Bookshelf
   
3. dockerを起動する :
    ```bash
    cd db
    docker-compose up -d

4. プロジェクトをビルドする：
    ```bash
    cd ..
    ./gradlew build

5. アプリケーションを実行する :
    ```bash
    ./gradlew run

## API一覧
以下は、APIの使用例です。詳細なドキュメントは、各APIエンドポイントのリンクをクリックしてください。

<details><summary>書籍一覧取得</summary>

- **リクエスト**:
  `GET /book/`
- **パラメータ**:
なし
- **レスポンス**:
  200 OK, JSON形式の書籍リスト 
- **cURLでの実行例**:
  ```bash
  curl -X GET "http://localhost:8080/book/ -H "accept: application/json"
</details>

<details><summary>著者IDに紐づく書籍一覧取得</summary>

- **リクエスト**:
  `GET /book/{authorId}`
- **パラメータ**:
  `authorId`
- **レスポンス**:
  200 OK, JSON形式の書籍リスト
- **cURLでの実行例**:
  ```bash
  curl -X GET "http://localhost:8080/book/2 -H "accept: application/json"
</details>

<details><summary>キーワード(タイトル)に紐づく書籍一覧取得</summary>

- **リクエスト**:
  `GET /book/keyword={keyword}`
- **パラメータ**:
  `keyword`
- **レスポンス**:
  200 OK, JSON形式の書籍リスト
- **cURLでの実行例**:
  ```bash
  curl -X GET "http://localhost:8080/book/keyword=猫 -H "accept: application/json"
</details>


<details> <summary>書籍追加</summary>

- **リクエスト**:
  `POST /book/add`
- **パラメータ**:
   ```bash
  {
    "title": "海辺のカフカ",
    "authorId": 1,
    "publicationDate": "2002-09-12"
   }
- **レスポンス**:書籍が追加されたことを示す文字列
- **cURLでの実行例**:
   ```bash
   curl -X POST "http://localhost:8080/book/add" -H "Content-Type: application/json" -d '{
    "title": "海辺のカフカ",
    "authorId": 1,
    "publicationDate": "2002-09-12"
   }
</details>

<details><summary>書籍更新</summary>

- **リクエスト**:
  `POST /book/update`
- **パラメータ**:
   ```bash
   {
    "bookId": 5,
    "title": "こころ",
    "authorId": 2,
    "publicationDate": "1914-08-11"
   }
- **レスポンス**:書籍が更新されたことを示す文字列
- **cURLでの実行例**:
   ```bash
   curl -X POST "http://localhost:8080/book/update" -H "Content-Type: application/json" -d '{
    "bookId": 5,
    "title": "こころ",
    "authorId": 2,
    "publicationDate": "1914-08-11"
   }
</details>

<details> <summary>書籍削除</summary>

- **リクエスト**:
  `POST /book/delete`
- **パラメータ**:
   ```bash
   {
    "bookId": 5
   }
- **レスポンス**:書籍が更新されたことを示す文字列
- **cURLでの実行例**:
   ```bash
   curl -X POST "http://localhost:8080/book/delete" -H "Content-Type: application/json" -d '{
    "bookId": 5
   }
</details>

<details><summary>著者一覧取得</summary>

- **リクエスト**:
  `GET /author/`
- **パラメータ**:
  なし
- **レスポンス**:
  200 OK, JSON形式の書籍リスト
- **cURLでの実行例**:
  ```bash
  curl -X GET "http://localhost:8080/author/ -H "accept: application/json"
</details>

<details><summary>キーワード(名前)に紐づく著者一覧取得</summary>

- **リクエスト**:
  `GET /author/keyword={keyword}`
- **パラメータ**:
  `keyword`
- **レスポンス**:
  200 OK, JSON形式の書籍リスト
- **cURLでの実行例**:
  ```bash
  curl -X GET "http://localhost:8080/author/keyword=村上 -H "accept: application/json"
</details>

<details><summary>著者追加</summary>

- **リクエスト**:
  `POST /author/add`
- **パラメータ**:
   ```bash
   {
    "name": "太宰　治",
    "description": ""
   }
- **レスポンス**:著者が追加されたことを示す文字列
- **cURLでの実行例**:
   ```bash
   curl -X POST "http://localhost:8080/author/add" -H "Content-Type: application/json" -d '{
    "name": "太宰　治",
    "description": ""
   }
</details>

<details><summary>著者更新</summary>

- **リクエスト**:
  `POST /author/update`
- **パラメータ**:
   ```bash
   {
    "authorId": 5,
    "name": "クリストファー・パオリーニ",
    "description": ""
   }
- **レスポンス**:書籍が更新されたことを示す文字列
- **cURLでの実行例**:
   ```bash
   curl -X POST "http://localhost:8080/author/update" -H "Content-Type: application/json" -d '{
    "authorId": 5,
    "name": "クリストファー・パオリーニ",
    "description": ""
   }
</details>

<details> <summary>著者削除</summary>

- **リクエスト**:
  `POST /author/delete`
- **パラメータ**:
   ```bash
   {
    "authorId": 5
   }
- **レスポンス**:書籍が更新されたことを示す文字列
- **cURLでの実行例**:
   ```bash
   curl -X POST "http://localhost:8080/book/delete" -H "Content-Type: application/json" -d '{
    "authorId": 5
   }
</details>

## テクノロジー
プロジェクトで使用している主な技術やライブラリについて記載します。

- **Kotlin (corretto-21)**
- **Spring Boot (3.2.3)**
- **jOOQ (3.17.22)**
- **Postgres (14.0-alpine)**

## 連絡先
プロジェクトに関する質問やフィードバックがある場合は、GitHub Issuesを通じてお問い合わせください。