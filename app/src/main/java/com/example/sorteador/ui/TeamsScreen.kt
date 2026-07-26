package com.example.sorteador.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sorteador.model.Team

@Composable
fun TeamsScreen(
    teams: List<Team>,
    onDrawTeams: (numberOfTeams: Int, playersPerTeam: Int) -> Unit,
    onStartTournament: () -> Unit
) {
    var numberOfTeamsText by remember { mutableStateOf("4") }
    var playersPerTeamText by remember { mutableStateOf("5") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Sorteio de Times", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = numberOfTeamsText,
                onValueChange = { numberOfTeamsText = it },
                label = { Text("Nº de Times") },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = playersPerTeamText,
                onValueChange = { playersPerTeamText = it },
                label = { Text("Jog./Time") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    val numTeams = numberOfTeamsText.toIntOrNull() ?: 4
                    val perTeam = playersPerTeamText.toIntOrNull() ?: 5
                    onDrawTeams(numTeams, perTeam)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Sortear")
            }

            if (teams.isNotEmpty()) {
                Button(
                    onClick = onStartTournament,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Criar Torneio")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(teams) { team ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = team.name,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "★ ${team.totalStars}",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Exibe o goleiro
                        Text(
                            text = "🧤 Goleiro: ${team.goalkeeper?.name ?: "Sem goleiro"}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Exibe os jogadores de linha
                        Text(
                            text = "🏃 Linha:",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        team.linePlayers.forEach { player ->
                            Text(
                                text = "  • ${player.name} (${player.stars}★)",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}
