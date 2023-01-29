# ShareIt
## Описание
Сервис для аренды вещей, что-то типа Авито. Можно искать по фрагментам описания или названия предмета. Если искомая вещь не найдена, можно создать запрос на аренду вещи, другие участники могут просматривать такие запросы. Платность аренды не предусмотрена. 

## Стек
Docker 2.2, Java 11, Maven 4.0.0, PostgreSQL 13.7-alpine, Spring Boot 2.7.2, RestTemplate, JUnit 

  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original-wordmark.svg" title="Spring" alt="Spring" width="30" height="30"/>&nbsp;
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original-wordmark.svg" title="postgresql" alt="postgresql" width="30" height="30"/>&nbsp;
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/docker/docker-original-wordmark.svg" title="docker" alt="docker" width="30" height="30"/>&nbsp;
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original-wordmark.svg" title="java" alt="java" width="30" height="30"/>&nbsp;
  <img src="https://raw.githubusercontent.com/junit-team/junit5/86465f4f491219ad0c0cf9c64eddca7b0edeb86f/assets/img/junit5-logo.svg" title="junit" alt="junit" width="30" height="30"/>&nbsp;
  <img src="https://www.svgrepo.com/show/373829/maven.svg" title="maven" alt="maven" width="25" height="40"/>&nbsp;
    <img src="https://voyager.postman.com/logo/postman-logo-orange-stacked.svg" title="postman" alt="postman" width="40" height="30"/>&nbsp;

### Подробнее

- микросервисное приложение. Состоит из двух сервисов: основного - server со всей бизнес логикой, и фильтра gateway, валидирующего все входные данные в сервер. Третьим сервисом идет БД postreSQL.

- к предметам добавлена возможность оставлять комментарии


## Установка

```bash

```

## Как использовать

```bash

```

## Участие в проекте

Если хочется и нужна практика - не стесняйся, планов много.

### Планы по улучшению
- [ ] написать документацию где необходимо
- [ ] заполнить раздел с инструкцией по установке и использованию
- [ ] разделить/добавить свои логи на info, trace, debug
- [ ] открутить прект от workflow яндекса и перенести в свой репозиторий, чтобы вдруг яндекс там ничего не обновил и приложение не перестало проходить проверки.
### Планы по возможному расширению
- [ ] прикрутить какой-нибудь интерфейс
- [ ] добавить сервис аутентификации на spring security
- [ ] добавить возможность сохранения фотографий
- [ ] подумать о добавлении брокера сообщений 
- [ ] запаковать все в .exe так, чтобы кто угодно смог установить и потыкать
