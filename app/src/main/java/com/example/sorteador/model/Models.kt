package com.example.sorteador.model

import java.util.UUID

// Define a posição do jogador
enum class PlayerPosition {
    LINE,          // Linha
    GOALKEEPER     // Goleiro
}

// Representa cada jogador cadastrado
data class Player(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val position: PlayerPosition,
    val stars: Int, // Classificação de 1 a 5 estrelas
    var isSelected: Boolean = true // Define se entra no sorteio atual
)

// Representa um time montado após o sorteio
data class Team(
    val id: Int,
    val name: String,
    val goalkeeper: Player?,
    val linePlayers: List<Player> = emptyList()
) {
    // Soma total de estrelas do time para verificação de equilíbrio
    val totalStars: Int
        get() = (goalkeeper?.stars ?: 0) + linePlayers.sumOf { it.stars }
}

// Representa uma partida do torneio/chaveamento
data class Match(
    val id: String = UUID.randomUUID().toString(),
    val homeTeam: Team,
    val awayTeam: Team,
    var homeGoals: Int = 0,
    var awayGoals: Int = 0,
    var isFinished: Boolean = false
)

// Representa as estatísticas de um time no painel de classificação
data class TeamStats(
    val teamName: String,
    var points: Int = 0,
    var matchesPlayed: Int = 0,
    var wins: Int = 0,
    var draws: Int = 0,
    var losses: Int = 0,
    var goalsFor: Int = 0,
    var goalsAgainst: Int = 0
) {
    // Saldo de gols
    val goalDifference: Int get() = goalsFor - goalsAgainst
}
