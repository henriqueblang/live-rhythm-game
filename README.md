[![Build Status](https://travis-ci.org/henriqueblang/live-rhythm-game.svg?branch=main)](https://travis-ci.org/henriqueblang/live-rhythm-game)

# live-rhythm-game
live! é um jogo de ritmo com *beatmaps* gerados automaticamente.

--- 
## Descrição
Projeto desenvolvido para a disciplina de Engenharia de Software, utilizando conceitos como POO, Design Pattern e Test Driven Development. O projeto utiliza as seguintes ferramentas/linguagens:
* [Java](https://openjfx.io)
* [Framework JavaFX](https://openjfx.io)
* [IDE Eclipse](https://www.eclipse.org/downloads/)
* [MySQL](https://www.mysql.com)

O jogo é do gênero de [rythm games](https://www.google.com/search?client=opera&q=rhythm+games&sourceid=opera&ie=UTF-8&oe=UTF-8), mais parecido com o modo osu!mania do jogo [osu!](https://osu.ppy.sh/home), ou então o jogo [Guitar Hero](https://pt.wikipedia.org/wiki/Guitar_Hero_(série)), que consiste em realizar alguma ação no ritmo da música. 

O jogo contém 4 faixas, cada uma representando uma nota, e em cada uma delas poderá descer uma nota,

---

## Funcionalidades 

Como outros jogos de ritmo, o live! apresenta funcionalidades como jogar um beatmap, listar sua biblioteca de música, alternar entre dificuldades, efeitos incríveis, e muito mais!

Algumas funcionalidades chaves existentes no jogo são:

* Seleção, listagem, e armazenamento de músicas
* Geração automatica do cenário
* Ajustes de configuração de jogabilidade

---

## Bibliotecas 

As bibliotecas utilizadas no projeto são:

* [javafx-controls](https://mvnrepository.com/artifact/org.openjfx/javafx-controls)
* [javafx-fxml](https://mvnrepository.com/artifact/org.openjfx/javafx-fxml)
* [gson](https://mvnrepository.com/artifact/com.google.code.gson/gson)
* [junit](https://mvnrepository.com/artifact/junit/junit/4.12)
* [mysql-connector-java](https://mvnrepository.com/artifact/mysql/mysql-connector-java)
* [mockito-core](https://mvnrepository.com/artifact/org.mockito/mockito-core)
* [animatefx](https://mvnrepository.com/artifact/io.github.typhon0/AnimateFX/1.2.0)
* [jlayer](https://mvnrepository.com/artifact/javazoom/jlayer/1.0.1)
* [javamp3](https://mvnrepository.com/artifact/fr.delthas/javamp3)
* [commons-io](https://mvnrepository.com/artifact/commons-io/commons-io)
* [mp3spi](https://mvnrepository.com/artifact/com.googlecode.soundlibs/mp3spi)
* [controlsfx](https://mvnrepository.com/artifact/org.controlsfx/controlsfx)

--- 

## Ferramentas

As seguintes ferramentas estão sendo utilizadas para a construção do projeto:

* [Maven](https://maven.apache.org)
* [JUnit](https://junit.org/junit5/)
* [Mockito](https://site.mockito.org)

---

## Como usar

Para que seja possível executar o jogo é necessário estar com um MySQL server no host em que o .jar está sendo executado. A configuração do MySQL deve ser:
* Host: `localhost`
* Porta: `3306`
* User: `root` 
* Password: `admin`
* DB Model: [`model.mwb`](https://github.com/henriqueblang/live-rhythm-game/blob/main/database/model.mwb)

Com o Banco de Dados *Running*, é necessário apenas executar o .jar, uma janela com o jogo se abrirá e você poderá usufruir do live!

Agora, no menu principal, será apresentado 4 botões, além de dados sobre suas sessões de jogos anteriores no canto superior esquerdo:

![Play](/docs/img/menu.PNG "Play Button")

* **Play:** Será listado as músicas presentes no banco de dados, assim como seu High Score, e a seleção de dificuldades para cada uma. Ao clicar em *Confirm* o jogo começará em um timer de 3 segundos! ![Play](/docs/img/play.PNG "Play Button")

* **Options:** Nas opções, será possível alterar seus atalhos do teclado, e também a velocidade em que a nota descerá nas faixas. ![Options](/docs/img/options.PNG "Options Button")

* **Add**: No botão adicionar, será possível adicionar novas músicas ao banco de dados. Além disso, para que o usuário possa ter uma melhor experiência na listagem das músicas, é possível selecionar uma *thumbnail*, e também selecionar em qual tempo a música será tocada ao ser selecionada.  ![Add](/docs/img/add.PNG "Add Button")

* **Quit:** O botão mais triste do jogo (ᗒᗣᗕ)՞, é nele que você encerra sua carreira, esperamos que momentaneamente, no live!

---
## Como jogar
Caso esteja começando nos jogos de ritmo, a premissa é bem simples: clique na tecla correspondente à faixa em que a nota está chegando. Explicando um pouco mais sobre cada elemento do jogo:

![Game](/docs/img/game.PNG "Game")

1. **Pontuação:** Será mostrada sua pontuação total até agora, acertar as notas fará com que sua pontuação aumente!
2. **Nota:** É o elemento que deve ter sua total atenção, a hora correta de pressionar o botão da faixa é no momento em que a nota se encontra na linha de julgamento.
3. **Health Points:** Basicamente é sua vida. Caso falhe muitas vezes, dependendo do tamanho da música, você perde e retorna à seleção de músicas. 
4. **Score da nota:** Toda nota que é *hitada* recebe um *Score* baseado na proximidade da nota e a linha de julgamento, quanto menor for a distância entre esses elementos, maior a pontuação. Do melhor *Score* para pior: **Perfect**, **Great**, **Good**, **Bad**, e caso não acerte a nota, **Miss**. 
5. **Linha de julgamento:** É onde você deve deve esperar a nota chegar para que possa apertar o botão correspondente à faixa. 
6. **Combo:** É a quantidade de notas que foram *hitadas* em sequência! Atinja 15 de combo e tenha uma surpresa. (`･ω･´)

--- 

## Requisitos 
* Maven
* Java 
* MySQL

### Clone

Clone esse repositório em sua máquina:

`git clone https://github.com/henriqueblang/live-rhythm-game.git` 

### Comandos

Dentro da pasta root do projeto, que o pom.xml se encontra, execute:
`mvn clean package`

> Antes deste próximo comando, certifique-se que o servidor MySQL está *up and running*. Caso ainda não fez o set-up, veja o [Como usar](#Como-usar)

Navegue até a pasta target e execute o .jar:

`java -jar live-rythm-game.jar`