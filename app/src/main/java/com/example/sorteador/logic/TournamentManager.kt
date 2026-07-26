package com.example.sorteador.logic

import com.example.sorteador.model.Match
import com.example.sorteador.model.Team
import com.example.sorteador.model.TeamStats

class TournamentManager(val teams: List<Team>) {

    val matches = mutableListOf<Match>()
    private val standings = mutableMapOf<String, TeamStats>()

    init {
        // Inicializa a tabela de classificação para cada time
        teams.forEach { standings[it.name] = TeamStats(teamName = it.name) }
        generateRoundRobinMatches()
    }

    // Gera os confrontos no formato todos contra todos
    private fun generateRoundRobinMatches() {
        matches.clear()
        for (i in teams.indices) {
            for (j in i + 1 until teams.size) {
                matches.add(Match(homeTeam = teams[i], awayTeam = teams[j]))
            }
        }
    }

    // Atualiza o resultado de um jogo e recalcula a pontuação
    fun updateMatchResult(matchId: String, homeGoals: Int, awayGoals: Int) {
        val match = matches.find { it.id == matchId } ?: return
        match.homeGoals = homeGoals
        match.awayGoals = awayGoals
        match.isFinished = true

        recalculateStandings()
    }

    // Recalcula pontos, vitórias, empates, derrotas e saldo de gols
    private fun recalculateStandings() {
        standings.values.forEach {
            it.points = 0
            it.matchesPlayed = 0
            it.wins = 0
            it.draws = 0
            it.losses = 0
            it.goalsFor = 0
            it.goalsAgainst = 0
        }

        for (match in matches.filter { it.isFinished }) {
            val home = standings[match.homeTeam.name] ?: continue
            val away = standings[match.awayTeam.name] ?: continue

            home.matchesPlayed++
            away.matchesPlayed++
            home.goalsFor += match.homeGoals
            home.goalsAgainst += match.awayGoals
            away.goalsFor += match.awayGoals
            away.goalsAgainst += match.homeGoals

            when {
                match.homeGoals > match.awayGoals -> {
                    home.wins++
                    home.points += 3
                    away.losses++
                }
                match.awayGoals > match.homeGoals -> {
                    away.wins++
                    away.points += 3
                    home.losses++
                }
                else -> {
                    home.draws++
                    home.points += 1
                    away.draws++
                    away.points += 1
                }
            }
        }
    }

    // Retorna a tabela ordenada por Pontos > Vitórias > Saldo de Gols > Gols Pró
    fun getSortedStandings(): List<TeamStats> {
        return standings.values.sortedWith(
            compareByDescending<TeamStats> { it.points }
                .thenByDescending { it.wins }
                .thenByDescending { it.goalDifference }
                .thenByDescending { it.goalsFor }
        )
    }
}
