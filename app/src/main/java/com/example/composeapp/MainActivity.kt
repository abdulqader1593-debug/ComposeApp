package com.example.composeapp

import android.R
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.composeapp.ui.theme.ComposeAppTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//
private const val TAG = "MainActivity"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Coroutine
//        GlobalScope.launch {
//            val suceed = makeApiCall() // immediately
//            Log.d(TAG, "onCreate: Suceed: $suceed")
//        }

        lifecycleScope.launch {
            val suceed = makeApiCall() // immediately
            Log.d(TAG, "onCreate: Suceed: $suceed")
        }

        setContent {
            ComposeAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    print(innerPadding)
//                    var counterState by remember {  mutableStateOf(Counter1()) }
//                    Counter(
//                        counter = counterState,
//                        onIncrement = {
//                        //counter.value = counter.value + 1
//                        // counter1 = counter1 +1
//                        val previous = counterState.newValue
//                        //counterState.value.newValue = counterState.value.newValue + 1
//                        val newCounter = counterState.copy(newValue = previous + 1)
//                            counterState = newCounter
//                    })
//                    Text(
//                        text = "you  have incremented the counter to :$counterState"
//                    )
                    CourseInfo()
                }
            }
        }
    }

    suspend fun makeApiCall():  Boolean{
        delay(30000L)
        return true
    }

    // Compositino, Recomposition
    @Composable
    fun GetListStateLess(
        counter: Counter1 = Counter1(newValue = 10),
        onIncrementCounter: () -> Unit
    ){
        LaunchedEffect(Unit) {
            makeApiCall()
        }

        Counter(
            counter = counter,
            onIncrement = onIncrementCounter
        )
    }

    @Composable
    fun GetList(){
       // var counterState = remember {  mutableStateOf(Counter1()) }
        var counterState by remember {  mutableStateOf(Counter1()) }
       Counter(
           counter = counterState,
           onIncrement = {
               //counter.value = counter.value + 1
               // counter1 = counter1 +1
               val previous = counterState.newValue
               //counterState.value.newValue = counterState.value.newValue + 1
               val newCounter = counterState.copy(newValue = previous + 1)
               counterState = newCounter
           }
       )
    }

    data class Counter1(
        val newValue: Int = 0,
        val updated: Long = System.currentTimeMillis()
    )
    val counterSaver = object: Saver<Counter1, String>{
        override fun SaverScope.save(value: Counter1): String {
            return "${value.newValue},${value.updated}"
        }

        override fun restore(value: String): Counter1? {
            val newValue = value.split(",")[0]
            val newValueParsed = newValue.toInt()
            val updated = value.split(",")[1]
            return Counter1(
                newValue = newValueParsed,
                updated = updated.toLong()
            )
        }

    }
    @Composable
    fun Counter(
        counter: Counter1,
        onIncrement: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // var counter = remember {  mutableStateOf(0) }
            //var counter by remember {  mutableStateOf(0) }
          //  var counterState = rememberSaveable(saver = counterSaver) { Counter1() }

            Text(
                text = "${counter.newValue}"
            )
            Button(
                onClick = {
                  onIncrement()
                }
            ) {
                Text(
                    text = "Increment"
                )
            }
        }



    }

    data class ShoppingItem1(
        val title:String = "Coffee",
        val price: Double = 3.50
    )

    val shoppingSaver = object: Saver<ShoppingItem1,String> {
        override fun SaverScope.save(value: ShoppingItem1): String? {
            return "${value.title}|${value.price}"
        }

        override fun restore(value: String): ShoppingItem1? {
            val parts = value.split("|")
            return ShoppingItem1(
                parts[0],
                parts[1].toDouble()
            )
        }
    }


    @Composable
    fun ShoppingCartScreen(){
        var item by rememberSaveable(stateSaver = shoppingSaver) {
            mutableStateOf(ShoppingItem1("Coffee", 3.50))
        }

        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Title: ${item.title}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Price: ${item.price}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    item = item.copy(
                        price = item.price + 0.50
                    )
                }
            ) {
                Text(text = "Increase price ($0.50)")
            }
        }
    }


    //
    data class Course(
        val subject:String,
        val tenure:String,
        val fee: Double
    )

    val courseSaver = object : Saver<Course,String>{
        override fun SaverScope.save(value: Course): String? {
            return "${value.subject}|${value.tenure}|${value.fee}"
        }

        override fun restore(value: String): Course? {
            val restoredValue = value.split("|")
            return Course(
                restoredValue[0],
                restoredValue[1],
                restoredValue[2].toDouble()
            )
        }

    }

    val courseListSaver = listSaver<Course, Any>(
        save = { course ->
            listOf(course.subject,course.tenure,course.fee)
        },
        restore = { restoredList ->
            Course(
                restoredList[0].toString(),
                restoredList[1].toString(),
                restoredList[2] as Double
            )
        }
    )

    @Composable
    fun CourseInfo(){
        var courseInfo by rememberSaveable(stateSaver = courseSaver) {
            mutableStateOf(Course("JAVA","3 MONTHS",19.99))
        }

        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {
            Text(
                text = "Course Name : ${courseInfo.subject}",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Tenure: ${courseInfo.tenure}",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Fee: ${courseInfo.fee}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    courseInfo = courseInfo.copy(
                        fee = courseInfo.fee + 2
                    )
                }
            ) {
                Text(text = "Increase fee by 2$")
            }
        }
    }

    @Composable
    fun composePractise(){
        var name by remember { mutableStateOf("Abdul") }
        var name1 by remember { mutableStateOf("Murtaza") }

        LaunchedEffect(Unit) {
            delay(3000)
            name = "Qadir"
            delay(3000)
            name = "Abdul"
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly

        ) {
                        Greeting(
                            name = name,
                            modifier = Modifier
                        )
                        Greeting(
                            name = name1,
                            modifier = Modifier
                        )

        }
    }

    //Endlessly Toggling State
    @Composable
    fun ComposePractise(){
        var name by remember { mutableStateOf("abdul") }
        var name1 by remember { mutableStateOf("murtaza") }

        LaunchedEffect(Unit) {

            while(true){
                delay(1000L)
                if(name == "abdul"){
                    name = "qader"
                }else{
                    name = "abdul"
                }

            }

        }

        Column(modifier = Modifier.fillMaxSize(),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly) {
            Text(
                text = name, color = Color.White, style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = name1, color = Color.White, style = MaterialTheme.typography.headlineLarge
            )
        }
    }

    //Controlling coroutine with LaunchedEffect Key
    @Composable
    fun ControlledCoroutine(){

        var name by remember { mutableStateOf("abdul") }
        var name1 by remember { mutableStateOf("abdu") }
        var isRunning by remember { mutableStateOf(true) }

        LaunchedEffect(isRunning, name) {
                while(isRunning){
                    delay(1000L)
                    if(name == "abdul"){
                        name = "qader"
                    }else{
                        name ="abdul"
                    }
                }
        }
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
            ) {
            Text(
                text = name, color = Color.White
            )
            Button(
                onClick = {
                    isRunning = !isRunning
                }
            ) {
                Text(text = if(isRunning) "Pause" else "Resume")
            }
        }
    }

    //Split your ControlledCoroutine() into two smaller functions:
    //Stateless UI Composable
    //Stateful Screen Composable
    @Composable
    fun NameControlContent(
        displayName: String,
        isRunning: Boolean,
        onToggleClick: () ->Unit
    ) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = displayName, color = Color.White
            )
            Button(
                onClick = onToggleClick
            ) {
                Text(text = if(isRunning) "Pause" else "Resume")
            }
        }
    }

    @Composable
    fun ControlledCoroutineScope(){
        var name by remember { mutableStateOf("abdul") }
        var isRunning by remember { mutableStateOf(true) }

        LaunchedEffect(isRunning) {
            while(isRunning){
                delay(1000L)
                name = if(name == "abdul") "qader" else "abdul"
            }
        }

        NameControlContent(
            displayName = name,
            isRunning = isRunning,
            onToggleClick = {isRunning = !isRunning}
        )
    }

//practise
    // State Hoisting

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        ComposeAppTheme {
            Greeting("Android")
        }
    }
}