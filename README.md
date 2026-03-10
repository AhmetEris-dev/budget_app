# BudgetApp

BudgetApp, kullanicilarin butce tanimlayip harcamalarini takip edebildigi, butce kullanim oranina gore otomatik uyari (alert) olusturan bir Spring Boot REST API projesidir.

## Projenin Amaci

- Kullanici bazli aylik/yillik butce yonetimi saglamak
- Harcama kayitlarini tarih araligina gore listelemek ve toplamini hesaplamak
- Butce limiti asimi veya kritik seviyelerde otomatik alert uretmek
- Guvenli erisim icin JWT (access + refresh) ve API key desteği sunmak

## Teknoloji Yigini

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA (Hibernate)
- Spring Security
- Flyway (database migration)
- PostgreSQL
- JWT (`jjwt`)
- Springdoc OpenAPI / Swagger UI
- Lombok
- Gradle (Wrapper ile)

## Mimari Moduller

`src/main/java/com/ahmete/budget_app` altinda baslica moduller:

- `auth`: Kayit, giris, token yenileme ve JWT islemleri
- `user`: Kullanici bilgileri
- `budget`: Butce olusturma/guncelleme ve aktif butce sorgulama
- `expense`: Harcama ekleme, listeleme, toplam/sunum verileri
- `alert`: Alert listeleme ve okunduya cekme
- `apikey`: API key olusturma ve dogrulama akisi
- `common`: Security config, ortak yardimci siniflar, OpenAPI config

## Veritabani ve Migration

Flyway migration dosyalari:

- `src/main/resources/db/migration/V1__init.sql`
- `src/main/resources/db/migration/V2__constraints_indexes.sql`
- `src/main/resources/db/migration/V3__auth_users_password_refresh_tokens.sql`

Baslica tablolar:

- `users`
- `api_keys`
- `budgets` (soft delete alanlari ile)
- `expenses`
- `alerts`
- `refresh_tokens`

## Guvenlik ve Kimlik Dogrulama

- Public endpointler:
  - `/api/v1/auth/**`
  - `/swagger-ui/**`
  - `/v3/api-docs/**`
- Diger endpointler kimlik dogrulama gerektirir.
- Desteklenen yontemler:
  - `Authorization: Bearer <access_token>`
  - `X-API-KEY: <api_key>`

## API Ozeti

### Auth

- `POST /api/v1/auth/register` - Kayit + token doner
- `POST /api/v1/auth/login` - Giris + token doner
- `POST /api/v1/auth/refresh` - Refresh token ile yeni token cifti uretir

### User

- `GET /api/v1/users/me` - Giris yapan kullanici bilgisi

### Budget

- `POST /api/v1/budgets` - Butce olusturur/gunceller
- `GET /api/v1/budgets/active?periodType=...&year=...&month=...` - Aktif butce

### Expense

- `POST /api/v1/expenses` - Harcama ekler
- `GET /api/v1/expenses?start=YYYY-MM-DD&end=YYYY-MM-DD` - Araliktaki harcamalar
- `GET /api/v1/expenses/total?start=YYYY-MM-DD&end=YYYY-MM-DD` - Araligin toplam harcamasi

### Alert

- `GET /api/v1/alerts?status=...&page=0&size=10` - Alert listeleme (sayfali)
- `PATCH /api/v1/alerts/{id}/read` - Alert'i okundu yapma

### API Key

- `POST /api/v1/api-keys` - Yeni API key olusturma

## Butce Izleme Mantigi

Yeni harcama eklendiginde sistem ilgili donemdeki toplam harcamayi hesaplar ve butce oranina gore alert uretir. Esikler `application.yml` altindaki `budget.alert.thresholds` alanindan okunur:

- `warning: 0.80`
- `exceeded: 1.00`
- `critical: 1.20`

## Gereksinimler

- JDK 17
- PostgreSQL (localhost:5432)
- Veritabani: `budget_app`
- Gradle Wrapper (`gradlew` / `gradlew.bat`)

## Konfigurasyon

Temel ayarlar:

- `src/main/resources/application.yml`
  - `server.port` (varsayilan: `9090`)
  - `spring.datasource.*` (DB baglantisi)
  - `spring.security.jwt.*` (JWT issuer/secret/sure)
  - `budget.alert.thresholds.*` (alert esikleri)

> Not: Mevcut konfigurasyonda gelistirme amacli ornek DB kullanici/sifre ve JWT secret degeri bulunur. Uretim ortaminda ortam degiskenleri ile guvenli sekilde yonetilmelidir.

## Calistirma

Windows (PowerShell):

```powershell
.\gradlew.bat bootRun
```

Test:

```powershell
.\gradlew.bat test
```

Build:

```powershell
.\gradlew.bat build
```

## API Dokumantasyonu

Uygulama ayaga kalktiktan sonra:

- Swagger UI: `http://localhost:9090/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:9090/v3/api-docs`

## Gelistirme Notlari

- Proje backend odakli bir REST API'dir (ayri frontend bulunmaz).
- Flyway ile sema degisiklikleri yonetilir; manuel tablo olusturma yerine migration dosyalari kullanilmalidir.
