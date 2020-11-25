# live!
[![Build Status](https://travis-ci.org/henriqueblang/live-rhythm-game.svg?branch=main)](https://travis-ci.org/henriqueblang/live-rhythm-game)
![Bower](https://img.shields.io/bower/l/mi?color=blue)

live! é um jogo de ritmo com beatmaps automaticamente gerados.

## Sumário
* [Descrição](#Descrição)
* [Funcionalidades](#Funcionalidades)
* [Bibliotecas](#Bibliotecas)
* [Ferramentas](#Ferramentas)
* [Como jogar](#Como-jogar)
* [Como usar](#Como-usar)


## Descrição
O projeto foi desenvolvido para a disciplina de Engenharia de Software, fazendo uso de conceitos como POO, Design Pattern e Test Driven Development. As seguintes ferramentas/linguagens foram utilizadas:
* [Java](https://www.java.com/en/)
* [Framework JavaFX](https://openjfx.io)
* [IDE Eclipse](https://www.eclipse.org/downloads/)
* [MySQL](https://www.mysql.com) 

O live! é do gênero de [rhythm games](https://www.google.com/search?client=opera&q=rhythm+games&sourceid=opera&ie=UTF-8&oe=UTF-8), e sua jogabilidade é similar a de jogos como o modo osu!mania do [osu!](https://osu.ppy.sh/home), [Guitar Hero](https://pt.wikipedia.org/wiki/Guitar_Hero_(série)), e [Beatmania](https://en.wikipedia.org/wiki/Beatmania).

O objetivo principal, numa sessão de jogo, é acertar as notas geradas ao ritmo da música sem perder todos seus pontos de energia. Acertos consecutivos garantem pontuação extra, estimulando o jogador a buscar pela pontuação perfeita para cada música.  

## Funcionalidades 

O live! apresenta funcionalidades como jogar beatmaps, listar a biblioteca de músicas, alternar dificuldades, customizar opções, efeitos incríveis, e muito mais!
Como um diferencial, é possível entrar com sua própria escolha de música e ter o beatmap das dificuldades existentes gerado automaticamente.

Algumas funcionalidades chave existentes no jogo são:
* Seleção, listagem, e armazenamento de músicas
* Geração automática de beatmap
* Ajustes de jogabilidade e atalhos

## Bibliotecas 

As bibliotecas utilizadas no projeto são:

* [animatefx](https://mvnrepository.com/artifact/io.github.typhon0/AnimateFX/1.2.0)
* [commons-io](https://mvnrepository.com/artifact/commons-io/commons-io)
* [controlsfx](https://mvnrepository.com/artifact/org.controlsfx/controlsfx)
* [gson](https://mvnrepository.com/artifact/com.google.code.gson/gson)
* [javafx-controls](https://mvnrepository.com/artifact/org.openjfx/javafx-controls)
* [javafx-fxml](https://mvnrepository.com/artifact/org.openjfx/javafx-fxml)
* [javamp3](https://mvnrepository.com/artifact/fr.delthas/javamp3)
* [jlayer](https://mvnrepository.com/artifact/javazoom/jlayer/1.0.1)
* [junit](https://mvnrepository.com/artifact/junit/junit/4.12)
* [mockito-core](https://mvnrepository.com/artifact/org.mockito/mockito-core)
* [mp3spi](https://mvnrepository.com/artifact/com.googlecode.soundlibs/mp3spi)
* [mysql-connector-java](https://mvnrepository.com/artifact/mysql/mysql-connector-java)

## Ferramentas

As seguintes ferramentas são utilizadas para a construção do projeto:
* [Maven](https://maven.apache.org)
* [JUnit](https://junit.org/junit5/)
* [Mockito](https://site.mockito.org)

## Como jogar

A premissa é simples e intuitiva: clique na tecla correspondente à faixa em que a nota está chegando. Quanto mais próximo do _timing_ correto, melhor sua pontuação.

![Game](/docs/img/game.PNG "Game")

1. **Pontuação:** será mostrada sua pontuação total até agora, acertar as notas fará com que sua pontuação aumente!
2. **Nota:** é o elemento que deve ter sua total atenção, a hora correta de pressionar o botão da faixa é no momento em que a nota se encontra na área de julgamento.
3. **Energy Points:** seus pontos de energia. Caso cheguem a 0, você perde e retorna ao menu principal. 
4. **Pontuação da nota:** toda nota, ao ser acertada, garante uma pontuação baseada na proximidade da nota e da área de julgamento. Quanto menor a distância, maior a pontuação. Do melhor *score* para pior: **Perfect**, **Great**, **Good**, **Bad**, e, caso não acerte a nota, **Miss**. 
5. **Área de julgamento:** área na qual você deve deve aguardar a nota chegar antes de pressionar o botão correspondente. 
6. **Combo:** quantidade de notas que foram acertadas em sequência. Atinja 15 de combo e tenha uma surpresa. (`･ω･´)

## Como usar

Para que seja possível executar o jogo é necessário estar com um MySQL server no host em que o .jar está sendo executado. A configuração do MySQL deve ser:
* Host: `localhost`
* Porta: `3306`
* User: `root` 
* Password: `admin`
* DB Model: [`model.mwb`](https://github.com/henriqueblang/live-rhythm-game/blob/main/database/model.mwb)

Com o servidor online, basta abrir o executável e aproveitar o live!

No menu principal, são apresentados quatro botões no centro e três dados no canto superior esquerdo da tela.

![Menu](/docs/img/menu.PNG "Menu")

* **Play:** sua biblioteca será listada, e ao selecionar uma música seu preview tocará, assim como a pontuação será exibida para cada dificuldade. Ao clicar em Confirm uma sessão de jogo começará!

![Play](/docs/img/play.PNG "Play Button")

* **Options:** nas opções, será possível modificar seus atalhos do teclado e a velocidade na qual as notas descerão nas faixas. 

![Options](/docs/img/options.PNG "Options Button")

* **Add**: novas músicas podem ser adicionadas à sua biblioteca. Além disso, visando uma melhor orientação na listagem da biblioteca, é possível selecionar uma *thumbnail* e qual segmento da música será tocado ao selecioná-la.  

![Add](/docs/img/add.PNG "Add Button")

* **Quit:** o botão mais triste do jogo (ᗒᗣᗕ)՞, é nele que você encerra sua carreira, esperamos que momentaneamente, no live!

### Requisitos 
* Maven
* Java 8
* MySQL

### Clone

Clone esse repositório em sua máquina:

```
git clone https://github.com/henriqueblang/live-rhythm-game.git
``` 

### Comandos

Para gerar o jar executável:

```
mvn clean package
```

> Antes de prosseguir, certifique-se que o servidor MySQL está *up and running*. Caso ainda não tenha feito o set-up, veja o [Como usar](#Como-usar).

Navegue até a pasta ```target``` e execute o live! através do comando:

```
java -jar live-rythm-game.jar
```
