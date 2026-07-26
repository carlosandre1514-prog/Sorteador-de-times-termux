package com.example.sorteador.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sorteador.model.Player
import com.example.sorteador.model.PlayerPosition

@Composable
fun PlayerScreen(
    players: List<Player>,
    onAddPlayer: (String, PlayerPosition, Int) -> Unit,
    onToggleSelect: (String) -> Unit
) {
    var nameText by remember { mutableStateOf("") }
    var isGoalkeeper by remember { mutableStateOf(false) }
    var stars by remember { mutableIntStateOf(3) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Cadastro de Jogadores", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nameText,
            onValueChange = { nameText = it },
            label = { Text("Nome do Jogador") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Checkbox(
                checked = isGoalkeeper,
                onCheckedChange = { isGoalkeeper = it }
            )
            Text("É Goleiro?")

            Spacer(modifier = Modifier.weight(1f))

            Text("Estrelas: $stars★")
            Slider(
                value = stars.toFloat(),
                onValueChange = { stars = it.toInt() },
                valueRange = 1f..5f,
                steps = 3,
                modifier = Modifier.width(120.dp)
            )
        }

        Button(
            onClick = {
                if (nameText.isNotBlank()) {
                    val pos = if (isGoalkeeper) PlayerPosition.GOALKEEPER else PlayerPosition.LINE
                    onAddPlayer(nameText, pos, stars)
                    nameText = ""
                    isGoalkeeper = false
                    stars = 3
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar Jogador")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))

        Text("Lista de Confirmados (${players.count { it.isSelected }}/${players.size})", style = MaterialTheme.typography.titleMedium)

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(players) { player ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Checkbox(
                        checked = player.isSelected,
                        onCheckedChange = { onToggleSelect(player.id) }
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(player.name, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            "${if (player.position == PlayerPosition.GOALKEEPER) "Goleiro" else "Linha"} - ${player.stars}★",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
