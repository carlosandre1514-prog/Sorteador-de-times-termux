package com.example.sorteador.logic

import com.example.sorteador.model.Player
import com.example.sorteador.model.PlayerPosition
import com.example.sorteador.model.Team

object TeamDrawer {

    fun drawTeams(
        availablePlayers: List<Player>,
        numberOfTeams: Int,
        playersPerTeam: Int
    ): List<Team> {
        // 1. Filtra apenas os jogadores que foram marcados para entrar no sorteio
        val selected = availablePlayers.filter { it.isSelected }

        // 2. Separa os goleiros e embaralha suas posições
        val goalkeepers = selected.filter { it.position == PlayerPosition.GOALKEEPER }.shuffled()
        
        // 3. Agrupa os jogadores de linha por nível de estrelas (5 a 1) e embaralha dentro de cada nível
        val linePlayers = selected.filter { it.position == PlayerPosition.LINE }
        val sortedLinePlayers = linePlayers
            .groupBy { it.stars }
            .entries
            .sortedByDescending { it.key }
            .flatMap { it.value.shuffled() }

        // 4. Cria os times e atribui os goleiros na sequência crescente (Time 1, Time 2, Time 3...)
        val teams = MutableList(numberOfTeams) { index ->
            val gk = if (index < goalkeepers.size) goalkeepers[index] else null
            Team(id = index + 1, name = "Time ${index + 1}", goalkeeper = gk)
        }

        val playerLists = List(numberOfTeams) { mutableListOf<Player>() }

        // 5. Distribui os jogadores de linha de forma equilibrada (Snake Draft: 1->2->3, 3->2->1)
        var currentTeam = 0
        var direction = 1
        val maxLinePlayersPerTeam = playersPerTeam - 1

        for (player in sortedLinePlayers) {
            var attempts = 0
            
            // Busca o próximo time que ainda tem vaga para linha
            while (playerLists[currentTeam].size >= maxLinePlayersPerTeam && attempts < numberOfTeams) {
                currentTeam += direction
                if (currentTeam >= numberOfTeams) {
                    currentTeam = numberOfTeams - 1
                    direction = -1
                } else if (currentTeam < 0) {
                    currentTeam = 0
                    direction = 1
                }
                attempts++
            }

            // Adiciona o jogador ao time encontrado
            if (playerLists[currentTeam].size < maxLinePlayersPerTeam) {
                playerLists[currentTeam].add(player)
            }

            // Alterna a direção da distribuição para manter o equilíbrio
            currentTeam += direction
            if (currentTeam >= numberOfTeams) {
                currentTeam = numberOfTeams - 1
                direction = -1
            } else if (currentTeam < 0) {
                currentTeam = 0
                direction = 1
            }
        }

        // 6. Retorna a lista final de times montados com seus respectivos jogadores
        return teams.mapIndexed { index, team ->
            team.copy(linePlayers = playerLists[index])
        }
    }
}
