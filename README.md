# Norte

App pessoal de Android para foco, hábitos, objetivos, treino e meditação. Feita por Alexandre Serras para uso próprio e partilhada com quem quiser experimentar.

Visual escuro com vidro fosco (liquid glass), grão de película e cores que mudam de página para página. Também tem modo claro.

## O que faz

- **Hoje**: a prioridade do dia, tarefas, lembretes diários e atalhos para o resto da app.
- **Foco**: sessões com uma esfera translúcida que acompanha o tempo. Pausas limitadas a 3 por sessão.
- **Hábitos**: marcar o que cumpriste hoje e ver a melhor sequência.
- **Objetivos**: com prazo. Os que expiram ficam cinzentos e podem ser retomados; os concluídos vão para o relatório.
- **Diário**: calendário com o humor de cada dia (vermelho, amarelo, verde).
- **Treino**: três circuitos guiados (Corpo inteiro, Core e Pernas) com 40 s de esforço e 20 s de descanso, boneco animado a mostrar cada exercício e meta semanal.
- **Meditação**: respiração guiada (Calma, Caixa, 4-7-8, Coerência) durante 3, 5 ou 10 minutos, com música calma.
- **Relatório diário às 21h** e uma sessão inicial de escrita sem filtros, que fica guardada para releres.
- **Tempo de ecrã por app**, lido do próprio Android (precisa de uma permissão tua).

A app vai aquecendo de azul para laranja conforme fores sendo consistente.

## Privacidade

Tudo fica guardado **no teu telemóvel**. A app não tem conta, não envia dados para nenhum servidor e não tem anúncios.

## Instalar (Android)

1. Vai ao separador **Releases** deste repositório e descarrega o ficheiro `.apk` da versão mais recente.
2. Abre o ficheiro no telemóvel. O Android vai pedir para permitires "instalar apps desconhecidas" à app de onde o abriste (Ficheiros, Chrome, etc.). Ativa.
3. Se aparecer o aviso do Play Protect ("app não verificada"), escolhe **Instalar mesmo assim**. É normal: a app não está na Play Store.
4. Na primeira abertura, escreve o teu nome.
5. Para ver o tempo de ecrã, carrega em **Ativar** e dá "Acesso ao uso" à Norte. Permite também as notificações, para os lembretes avisarem.

Só funciona em Android. Não há versão para iPhone.

## Aviso

É um projeto pessoal, feito sem equipa e testado em poucos aparelhos. Pode ter falhas. Se encontrares uma, abre uma **Issue** com uma captura de ecrã e o modelo do telemóvel.

## Como foi feita (para curiosos)

- A app inteira é um único ficheiro `index.html` (HTML, CSS e JavaScript, sem frameworks).
- É empacotada para Android com [Capacitor](https://capacitorjs.com/).
- O APK é compilado automaticamente pelo GitHub Actions (`.github/workflows/apk.yml`) a cada alteração.
- O tempo de ecrã vem de um pequeno plugin nativo em Java, na pasta `native/`.

Para compilares tu mesmo: faz fork deste repositório, ativa as Actions e corre o workflow **Compilar APK**. O APK fica em *Artifacts*.
