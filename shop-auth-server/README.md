### Pierwsze uruchamienie kontenera z Keycloakiem

`docker run -p 3000:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin -v //$(PWD)/realms:/opt/keycloak/data/import  --name shop-auth-server quay.io/keycloak/keycloak:23.0.4 start-dev --import-realm`

- aby uniknąć ręcznego wyklikiwania danych w panelu administracyjnym Keycloak, importujemy plik JSON z realmem `shop`
- plik znajduje się w katalogu `realms`
- import wymaga podmontowania ww. katalogu `realms` do folderu `/opt/keycloak/data/import` na kontenerze i przekazania opcji `--import-realms` na starcie
- w systemie Windows aby dało się podmontować folder o ścieżce relatywnej do bieżącej trzeba użyć konstrukcji z `$(PWD)` - żaden inny sposób nie działa 
- **w związku z tym ww. polecenie trzeba wykonać w katalogu `shop-auth-server` !** - inaczej import nie zadziała 
- nadajemy też kontenerowi nazwę `shop-auth-server`, tak aby łatwiej go było potem wystartować ponownie
- w razie potrzeby Keycloak jest dostępny w przeglądarce pod adresem: 'http://localhost:3000'

### Ponowne uruchomienie kontenera

`docker container start -a shop-auth-server`

- nie ma potrzeby przekazywać opcji jakie używaliśmy przy `docker run` - zostaną dołożone automatycznie
- w związku z tym przy każdym uruchomieniu będzie następował próba reimportu realmu `shop` 
- nie będzie to jednak problemem - bo w przypadku już istniejącego realmu o takiej nazwie import jest pomijany

### Uzyskiwanie access tokena - flow typu `client_credentials`

- razem z realmem `shop` importowani są także:
    - client o id `shop-client` i secret `VwpcoDqxMdTJDTWJBIEs6aa3W7EvPjZz` (ten klient nie ma przypisanej żadnej roli)
    - client o id `shop-admin-client` i secret `Z4s4D0nbBo6HeLu5p0VSmZsP9bMY6nwE` (ten klient ma przypisaną rolę `admin`)
- obaj klienci są stworzony dla flow typu `client_credentials`
- jest to najprostszy flow w OAuth2 umożliwiający autentykację jednego serwera przed drugim; klient autentykuje się w swoim imieniu a nie użytkownika
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

### Uzyskiwanie access tokena - flow typu `authorization_code`

- do uzyskiwania access tokena służy client `shop-users-client` (również on jest importowany razem z realmem `shop`)
- dane ww. klienta - id: `shop-users-client`, secret: `CeV1MQi7fgbCYr6kkRKvqDEUC9KIFIiv`  
- ww. klient jest stworzony dla flow typu `authorization_code` - w tym flow autoryzacji dla klienta dokonujemy w imieniu użytkownika
- by umożliwić taką autoryzację importowani są również dwaj użytkownicy:
    - `marcin` z hasłem `marcin` i przypisaną rolą `admin`
    - `tomasz` z hasłem `tomasz` (bez żadnej przypisanej roli)
- w tym flow pozyskiwanie access tokena przebiega w 3 krokach - najlepiej zrobić to za pomocą Postmana (choć ręcznie też oczywiście się da)
- w kroku 1 żądamy od użytkownika aby sam zautoryzował się na keycloaku poprzez otwarcie następującego URL:
`http://localhost:3000/realms/shop/protocol/openid-connect/auth?response_type=code&client_id=shop-users-client&redirect_uri=https%3A%2F%2Foauth.pstmn.io%2Fv1%2Fcallback`
   - w tym przypadku jako redirect_uri używamy adresu z domeny `oauth.pstmn.io` - po otwraciu takiego URL w przeglądarce zostaniemy przekierowani do Postmana  
- w kroku 2 jeżeli użytkownik zaloguje się poprawnie i zgodzi na przekazanie klientowi wskazanych uprawnień to wówczas zostanie przekierowany pod URL podany jako `request_uri` a dodatkowo jako query param otrzyma access code:
`https://oauth.pstmn.io/v1/callback?session_state=30d81aa1-9276-4070-a42d-fa53da1e4a79&iss=http%3A%2F%2Flocalhost%3A3000%2Frealms%2Fshop&code=ab598248-13d0-4ecf-9010-b257525105ae.30d81aa1-9276-4070-a42d-fa53da1e4a79.83d6e774-ce22-4f56-bddb-3c7736f29378`
- w kroku 3 aplikacja już bez pośrednictwa użytkownika wymienia uzyskany access code na access token za pomocą poniższego requestu:

```http request
POST http://localhost:3000/realms/shop/protocol/openid-connect/token
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code
&code=ab598248-13d0-4ecf-9010-b257525105ae.30d81aa1-9276-4070-a42d-fa53da1e4a79.83d6e774-ce22-4f56-bddb-3c7736f29378
&redirect_uri=https://oauth.pstmn.io/v1/callback
&client_id=shop-users-client
&client_secret=CeV1MQi7fgbCYr6kkRKvqDEUC9KIFIiv
```

### Usuwanie kontenera

`docker container rm shop-auth-server`
  `
### Method security - customizacja tokenów

- aby wprowadzić method security należy najpierw zmodyfikować odczytywanie nazwy zalogowanego użytkownika (principal name) z tokena
- domyślna implementacja zwróci nam bowiem nieprzyjazne w użyciu `id; dodatkowo będzie ono niespójne pomiędzy flowami client credentials i authorization code
- dlatego też wymuszamy aby nazwa była brana z pola `principal_username`

### `@PreAuthorize` / `@PostAuthorize`

- jeden z najprostszych sposobów wprowadzenia method security
- kontrola dostępu do metody odbywa się na podstawie wyrażenia przekazanego w adnotacji `@PreAuthorize` lub `@PostAuthorize`
- w przypadku `@PreAuthorize` kontrola odbywa się **PRZED** zawołaniem metody (tj. metoda nie zawoła się jeśli nie użytkownik nie ma uprawnień)
- w przypadku `@PostAuthorize` kontrola odbywa się **PO** zawołaniu metody (tj. metoda wykona się zawsze)
- `@PostAuthorize` daje z kolei możliwość ewaluacji uprawnień pod kątem wartości zwróconej z metody
- `@PreAuthorize` pozwala tylko na analizę argumentów wejściowych do metody
- w `ProductService` zostały użyte obie adnotacje:
    - `@PreAuthorize` - przy pobieraniu produktów należących do zadanego użytkownika sprawdzamy czy jest to ten sam użytkownik co zalogowany
    - `@PostAuthorize` - przy pobieraniu produktu o danym `id` sprawdzamy czy pobrany produkt należy do zalogowanego użytkownika
    - w obydwu przypadkach użytkownik mający rolę `admin` omija ww. reguły (ma dostęp do wszystkiego)

### `@PreFilter` / `@PostFilter`

- adnotacje te zamiast kontrolować czy użytkownik ma prawo do wykonania danej metody modyfikują dane wprowadzane do lub zwracane z metody
- `@PreFilter` pozwala na usunięcie z kolekcji przekazanej jako argument metody elementów, które nie spełniają wyrażenia przekazanego jako argument adnotacji
- jeżeli metoda ma więcej niż 1 argument to należy dodatkowo podać, który z nich poddajemy filtracji
- `@PostFilter` działa podobnie ale usuwa elementy z kolekcji zwróconej z metody
- w przypadku obydwu adnotacji kolekcja, która ma być poddana filtracji musi implementować metodę `remove`
- wspierane są tylko kolekcje należące do Java Collections
- adnotacji nie można zastosować w przypadku tablic
- w `ProductService` została użyta adnotacja `@PostFilter` -w celu odfiltrowania produktów nie należących do zalogowanego użytkownika (nie dotyczy admina :))
- w wyrażeniu użyty jest obiekt `filterObject` reprezentujący pojedynczy element zwróconej kolekcji
- jak widać wyrażenie ewaluowane jest oddzielnie dla każdego obiektu należącego do kolekcji
- `@PreFilter` / `@PostFilter` pozwalają często na uproszczenie implementacji reguł bezpieczeństwa, jednakże mają też słabe strony:
   - kolekcja najpierw musi być pobrana w całości a dopiero potem odfiltrowana co może źle wpłynąć na wydajność
   - odfiltrowanie zwracanej kolekcji uniemożliwia implementację stronicowania
  
### Programowa ewaluacja reguł bezpieczeństwa za pomocą interfejsu `PermissionEvaluator`

- wyrażenia SpEL używane do definiowania reguł w adnotacjach `@Pre/@Post Authorize/Filter`:
    - są niejednokrotnie skomplikowane i trudne w zrozumieniu oraz podatne na błędy
    - może też się okazać że są niewystarczające w bardziej skomplikowanych przypadkach
- w takich przypadkach pomocne może okazać się implementacji interfejsu `PermissionEvaluator`
- implementacje te umożliwiają definiowianie reguł udzielających odpowiedzi na jedno z dwóch pytań:
    - czy zalogowany użytkownik ma prawo do wykonania `akcji` na danym `obiekcie`
    - czy zalogowany użytkownik ma prawo do wykonania `akcji` na danym obiekcie o danym `typie` i `identyfikatorze`
- jak można łatwo zauważyć, pytania te odpowiadają metodom interfejsu `PermissionEvaluator`
- gdy obiekt `PermissionEvaluator` jest już dostępny w kontekście aplikacji Springa, to wówczas można zacząć używać wyrażeń `hasPermission`
- przykłady użycia znajdują się w `ProductService`
- w naszej aplikacji `PermissionEvaluator` został użyty w nieco bardziej zaawansowanej formie - z wykorzystaniem wzorca chain of responsibility
- takie podejście umożliwi wpięcie kilku różnych implementacji interfejsu `PermissionEvalutor`, tak aby każda z nich dotyczyła innej domeny 

### Method security a przetwarzanie wsadowe

- w przypadku przetwarzania wsadowego (np. zadania schedulowane) autoryzacja w zwykły sposób (np. poprzez token):
   - jest trudna w implementacji (pomijamy bowiem całą warstwę HTTP / Web)
   - jest niecelowa (zadania wsadowe nie są uruchamiane z zewnątrz systemu)
- w związku z powyższym próby zawołania zabezpieczonych metod przy takim przetwarzaniu zakończą się błędem dostępu
- najprostszym sposobem rozwiązania tego problemu jest ręczne wstrzyknięcie obiektu `Authentication` do `SecurityContext`
- przykład znajduje się w `ProductScheduler` - jest tam zdefiniowane periodyczne zadanie wyświetlające wszystkie produkty