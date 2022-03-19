package search

import java.io.File

fun main(args: Array<String>) {
    getData(args)
    getMap()
    chooseMenuOption()
}

object Input {
    var num = 0
    var linesList = mutableListOf<MutableList<String>>()
    var map = mutableMapOf<String, MutableList<Int>>()
}

// Present menu option to users.
fun chooseMenuOption() {
    while (true) {
        println("=== Menu ===")
        println("1. Find a person")
        println("2. Print all people")
        println("0. Exit")
        when (readLine()!!) {
            "0" -> break
            "1" -> searchOption()
            "2" -> printAll()
            else -> println("Incorrect option! Try again.")
        }
    }
    println("Bye!")
}

// Present search option to users.
fun searchOption() {
    println("Select a matching strategy: ALL, ANY, NONE")
    when (readLine()!!) {
        "ANY" -> searchAny()
        "ALL" -> searchAll()
        "NONE" -> searchNone()
        else -> {
            println("Invalid strategy!")
            return
        }
    }
}

// Get the list of people in the file and add to the Input lineList.
fun getData(args: Array<String>) {
    var data = ""
    if (args[0] == "--data") {
        data = args[1]
    }
    val fileName =
        "C:\\Users\\Administrator\\IdeaProjects\\Simple Search Engine\\Simple Search Engine\\task\\src\\search/$data"
    val lines = File(fileName).readLines()
    for (line in lines) {
        Input.linesList.add(line.split(" ").toMutableList())
    }
}

// Create a map based on the Input.lineList
fun getMap() {
    for ((index, line) in Input.linesList.withIndex()) {
        val lineCount = mutableListOf(index)
        for (word in line) {
            if (!Input.map.containsKey(word.lowercase())) {
                Input.map[word.lowercase()] = lineCount
            } else {
                Input.map[word.lowercase()] = (Input.map[word.lowercase()]!! + lineCount) as MutableList<Int>
            }

        }
    }
}

fun getUserInput() {
    println("Enter the number of people:")
    Input.num = readLine()!!.toInt()
    println("Enter all people:")
    for (i in 1..Input.num) {
        val lines = readLine()!!.split(" ").toMutableList()
        Input.linesList.add(lines)
    }
}

fun search() {
    println("Enter a name or email to search all suitable people.")
    val data = readLine()!!
    val searchResult = mutableListOf<MutableList<String>>()
    for (line in Input.linesList) {
        if (line.joinToString(" ").lowercase().contains(data.lowercase())) {
            searchResult.add(line)
        }
    }
    if (searchResult.isEmpty()) {
        println("No matching people found.")
        return
    }
    println("People found:")
    for (item in searchResult) {
        println(item.joinToString(" "))
    }
}

fun searchByInvert(input: String): MutableList<Int> {
    var searchResult = emptyList<Int>().toMutableList()
    if (Input.map.containsKey(input.lowercase())) {
        searchResult = Input.map[input.lowercase()]!!
    }
    return searchResult

}

// Get a list containing a list of search result
fun getSearchResult(): MutableList<MutableList<Int>> {
    println("Enter a name or email to search all matching people.")
    val inputList = readLine()!!.split(" ")
    val res = mutableListOf<MutableList<Int>>()
    for (input in inputList) {
        res.add(searchByInvert(input))
    }
    return res
}

// Implement search for any word in the input
fun searchAny() {
    val res = getSearchResult().flatten().toMutableList()
    printResult(res)
}

// Implement search for the whole input words.
fun searchAll() {
    val res = getSearchResult()
    val common = findCommon(res)
    printResult(common)
}

// Implement search for word not in the input.
fun searchNone() {
    val res = getSearchResult().flatten().toMutableList()
    val numberLineList = listOf(0 until Input.linesList.size).flatten()
    val difference = numberLineList.minus(res.toSet()).toMutableList()
    printResult(difference)

}

// Find the common list in a list of result
fun findCommon(res: MutableList<MutableList<Int>>): MutableList<Int> {
    val common = res.first().toMutableSet()
    for (i in res.indices) {
        common.retainAll(res[i].toSet())
    }
    return common.toMutableList()
}

// Print all the people in the file.
fun printAll() {
    println("=== List of people ===")
    for (line in Input.linesList) {
        println(line.joinToString(" "))
    }
}

// Print the result when the result list is available.
fun printResult(res: MutableList<Int>) {
    if (res.isEmpty()) {
        println("No matching people found.")
        return
    } else if (res.size == 1) {
        println("1 person found:")
    } else {
        println("${res.size} persons found:")
    }
    for (lineNum in res) {
        println(Input.linesList[lineNum].joinToString(" "))
    }
}