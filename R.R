#1 instalacja bibliotek
install.packages(c("readxl", "MCDA")) 

#2 użycie bibliotek
library(readxl)
library(MCDA)     

#3 przypisanie do zmiennej 'arkusz' tabeli z arkusza 'data-raw' w pliku 'praca_licencjacka.xlsx'
arkusz <- read_excel(path = "praca_licencjacka.xlsx", sheet = "data-raw", col_names = F) 

#4 przypisanie do zmiennej 'normalizedTable' znormalizowanej macierzy danych z arkusza, przy zastosowaniu metody normalizacji 'rescaling', czyli znanej nam formuły z podrozdziału 1.3.1.
normalizedTable <- normalizePerformanceTable(performanceTable = arkusz, normalizationTypes = c("rescaling","rescaling","rescaling","rescaling","rescaling","rescaling","rescaling","rescaling","rescaling","rescaling"))

#5 przypisanie do zmiennej 'kraj' listy państw, by wyświetlić je w rankingu
kraj <- c("Austria", "Belgia", "Bułgaria", "Chorwacja", "Cypr", "Czechy", "Dania", "Estonia", "Finlandia", "Francja", "Grecja", "Hiszpania", "Holandia", "Irlandia", "Litwa", "Luksemburg", "Łotwa", "Malta", "Niemcy", "Polska", "Portugalia", "Rumunia", "Słowacja", "Słowenia", "Szwecja", "Węgry", "Wielka Brytania", "Włochy")

#6 lista ważonej średniej realizacji kryteriów przez państwa
wynik <- weightedSum(performanceTable = normalizedTable, criteriaWeights = c(1,1,1,1,1,1,1,1,1,1))

#7 przypisanie do zmiennej 'df' data frame'a, który pokaże nam wyniki obliczeń
df <- data.frame(kraj, wynik)

#8 posortowanie wyników
result <- df[order(-df$wynik),]
pozycja <- 1:28
x <- data.frame(pozycja, result)

#9 wyświetlenie wyników
View(x)