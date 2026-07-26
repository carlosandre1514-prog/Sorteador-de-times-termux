package com.example.sorteador.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.sorteador.logic.TeamDrawer
import com.example.sorteador.logic.TournamentManager
import com.example.sorteador.model.Player
import com.example.sorteador.model.PlayerPosition
import com.example.sorteador.model.Team

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainAppScreen()
            }
        }
    }
}

@Composable
fun MainAppScreen() {
    // Lista inicial de exemplo (você pode editar ou adicionar novos na tela)
    val players = remember {
        mutableStateListOf(
            Player(name = "Carlos", position = PlayerPosition.LINE, stars = 4),
            Player(name = "Bruno", position = PlayerPosition.GOALKEEPER, stars = 3),
            Player(name = "Lucas", position = PlayerPosition.LINE, stars = 5),
            Player(name = "Diego", position = PlayerPosition.GOALKEEPER, stars = 4),
            Player(name = "Mateus", position = PlayerPosition.LINE, stars = 3),
            Player(name = "Gabriel", position = PlayerPosition.LINE, stars = 2),
            Player(name = "Felipe", position = PlayerPosition.LINE, stars = 4),
            Player(name = "André", position = PlayerPosition.LINE, stars = 5)
        )
    }

    var drawnTeams by remember { mutableStateOf<List<Team>>(emptyList()) }
    var tournamentManager by remember { mutableStateOf<TournamentManager?>(null) }
    var currentTab by remember { mutableIntStateOf(0) } // 0 = Jogadores, 1 = Times, 2 = Torneio

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentTab == 0,
                    onClick = { currentTab = 0 },
                    label = { Text("Jogadores") },
                    icon = { Text("👤") }
                )
                NavigationBarItem(
                    selected = currentTab == 1,
                    onClick = { currentTab = 1 },
                    label = { Text("Times") },
                    icon = { Text("⚽") }
                )
                NavigationBarItem(
                    selected = currentTab == 2,
                    onClick = { currentTab = 2 },
                    enabled = tournamentManager != null,
                    label = { Text("Torneio") },
                    icon = { Text("🏆") }
                )
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            when (currentTab) {
                0 -> PlayerScreen(
                    players = players,
                    onAddPlayer = { name, pos, stars ->
                        players.add(Player(name = name, position = pos, stars = stars))
                    },
                    onToggleSelect = { id ->
                        val index = players.indexOfFirst { it.id == id }
                        if (index != -1) {
                            players[index] = players[index].copy(isSelected = !players[index].isSelected)
                        }
                    }
                )
                1 -> TeamsScreen(
                    teams = drawnTeams,
                    onDrawTeams = { numTeams, perTeam ->
                        drawnTeams = TeamDrawer.drawTeams(players, numTeams, perTeam)
                        tournamentManager = null // Reseta torneio anterior ao sortear novos times
                    },
                    onStartTournament = {
                        if (drawnTeams.isNotEmpty()) {
                            tournamentManager = TournamentManager(drawnTeams)
                            currentTab = 2 // Vai direto para a aba de Torneio
                        }
                    }
                )
                2 -> tournamentManager?.let { manager ->
                    TournamentScreen(tournamentManager = manager)
                }
            }
        }
    }
}
