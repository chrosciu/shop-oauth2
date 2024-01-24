### Pierwsze uruchamienie kontenera z Keycloakiem

`docker run -p 3000:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin -v //$(PWD)/realms:/opt/keycloak/data/import  --name keycloak-shop quay.io/keycloak/keycloak:23.0.4 start-dev --import-realm`

- aby uniknąć ręcznego wyklikiwania danych w panelu administracyjnym Keycloak, importujemy plik JSON z realmem `shop`
- plik znajduje się w katalogu `realms`
- import wymaga podmontowania ww. katalogu `realms` do folderu `/opt/keycloak/data/import` na kontenerze i przekazania opcji `--import-realms` na starcie
- w systemie Windows aby dało się podmontować folder o ścieżce relatywnej do bieżącej trzeba użyć konstrukcji z `$(PWD)` - żaden inny sposób nie działa 
- nadajemy też kontenerowi nazwę `keycloak-shop`, tak aby łatwiej go było potem wystartować ponownie
- w razie potrzeby Keycloak jest dostępny w przeglądarce pod adresem: 'http://localhost:3000'

### Ponowne uruchomienie kontenera

`docker container start -i keycloak-shop`

- nie ma potrzeby przekazywać opcji jakie używaliśmy przy `docker run` - zostaną dołożone automatycznie
- w związku z tym przy każdym uruchomieniu będzie następował próba reimportu realmu `shop` 
- nie będzie to jednak problemem - bo w przypadku już istniejącego realmu o takiej nazwie import jest pomijany

### Uzyskiwanie access tokena

- razem z realmem `shop` importowany jest także client o id `client-shop` i secret `VwpcoDqxMdTJDTWJBIEs6aa3W7EvPjZz`
- client jest stworzony dla flow typu `client_credentials`
- jest to najprostszy flow w OAuth2 umożliwiający autentykację jednego serwera przed drugim; client autentykuje się w swoim imieniu a nie użytkownika
- aby uzyskać access token należy wykonać następujący request:

```http request
POST http://localhost:3000/realms/shop/protocol/openid-connect/token
Content-Type: application/x-www-form-urlencoded

grant_type=client_credentials
&client_id=shop-client
&client_secret=VwpcoDqxMdTJDTWJBIEs6aa3W7EvPjZz
```

- w odpowiedzi otrzymamy access token:

```json
{
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJTcW9oUm5TSXRVaHVuVHBQY0VpV3hJVGlVR3pxN1RFOEJFc0padUprTmxrIn0.eyJleHAiOjE3MDYxMTE5NjgsImlhdCI6MTcwNjExMTA2OCwianRpIjoiNjk3Mzc2YjktYTQyNy00MGU4LTgwMTAtMzI0YjU0ZGRkNjUxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDozMDAwL3JlYWxtcy9zaG9wIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjdiZTE4YWY2LTI0NDYtNDJmZC1iYzkyLTEyNzU5NWUwMjk2NiIsInR5cCI6IkJlYXJlciIsImF6cCI6InNob3AtY2xpZW50IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIvKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJkZWZhdWx0LXJvbGVzLXNob3AiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsImNsaWVudEhvc3QiOiIxNzIuMTcuMC4xIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzZXJ2aWNlLWFjY291bnQtc2hvcC1jbGllbnQiLCJjbGllbnRBZGRyZXNzIjoiMTcyLjE3LjAuMSIsImNsaWVudF9pZCI6InNob3AtY2xpZW50In0.gdnaWh9JTShVBMWc77xlBzERPp0FYVJJhfYHVNi_kyMx7HkOPZjtscZncblmL6CAO3aHbsoUZEDAhxzvGaZdfgxRbp9ukpkLnzBnYLpBgA7xzEc_Sk3wXQKxo9er6WvC0aWeGuNAAofE-5Ceo9gAm9mIj52nn2HFh1u4RDh0Yf_3r82eHAM1y3J2izb4s7NVZs5UBs2kTdtj0nB7NsZpx3Ldl8d7LAz3ya7OOnKLy1u1T_REPBzSPa4jrZpKHnf3wm9C71lp1NO7VJlyPZaOUgq6EaF9qvFc1U68SAd3bRV8iT8vsuZ-TVenPuUDsoRt24Onwmn82l-JrINL2F-waw",
    "expires_in": 900,
    "refresh_expires_in": 0,
    "token_type": "Bearer",
    "not-before-policy": 0,
    "scope": "email profile"
}
```

### Usuwanie kontenera

`docker container rm keycloak-shop`
  `

