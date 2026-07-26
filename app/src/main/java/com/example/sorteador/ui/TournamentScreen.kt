package com.example.sorteador.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sorteador.logic.TournamentManager
import com.example.sorteador.model.Match
import com.example.sorteador.model.TeamStats

@Composable
fun TournamentScreen(tournamentManager: TournamentManager) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var updateTrigger by remember { mutableIntStateOf(0) } // Para forçar o recompose ao salvar resultados

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Jogos") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Classificação") }
            )
        }

        when (selectedTab) {
            0 -> MatchesList(
                matches = tournamentManager.matches,
                onUpdateScore = { matchId, homeGoals, awayGoals ->
                    tournamentManager.updateMatchResult(matchId, homeGoals, awayGoals)
                    updateTrigger++
                }
            )
            1 -> StandingsTable(
                standings = tournamentManager.getSortedStandings(),
                trigger = updateTrigger
            )
        }
    }
}

@Composable
fun MatchesList(
    matches: List<Match>,
    onUpdateScore: (String, Int, Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(matches) { match ->
            var homeGoalsText by remember(match.id) { mutableStateOf(match.homeGoals.toString()) }
            var awayGoalsText by remember(match.id) { mutableStateOf(match.awayGoals.toString()) }

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(match.homeTeam.name, modifier = Modifier.weight(1f))

                        OutlinedTextField(
                            value = homeGoalsText,
                            onValueChange = { homeGoalsText = it },
                            modifier = Modifier.width(55.dp),
                            singleLine = true
                        )

                        Text(" x ", modifier = Modifier.padding(horizontal = 8.dp))

                        OutlinedTextField(
                            value = awayGoalsText,
                            onValueChange = { awayGoalsText = it },
                            modifier = Modifier.width(55.dp),
                            singleLine = true
                        )

                        Text(match.awayTeam.name, modifier = Modifier.weight(1f), alignment = Alignment.CenterHorizontally)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val home = homeGoalsText.toIntOrNull() ?: 0
                            val away = awayGoalsText.toIntOrNull() ?: 0
                            onUpdateScore(match.id, home, away)
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(if (match.isFinished) "Atualizar Placar" else "Salvar Resultado")
                    }
                }
            }
        }
    }
}

@Composable
fun StandingsTable(standings: List<TeamStats>, trigger: Int) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Time", style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(2f))
            Text("P", style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
            Text("J", style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
            Text("V", style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
            Text("SG", style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
        }

        Divider()

        LazyColumn {
            items(standings) { stats ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stats.teamName, modifier = Modifier.weight(2f))
                    Text("${stats.points}", modifier = Modifier.weight(1f))
                    Text("${stats.matchesPlayed}", modifier = Modifier.weight(1f))
                    Text("${stats.wins}", modifier = Modifier.weight(1f))
                    Text("${stats.goalDifference}", modifier = Modifier.weight(1f))
                }
                Divider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        }
    }
}
