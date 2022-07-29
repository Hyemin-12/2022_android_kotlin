package com.example.jsondeserializationstudy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

//data class MyJSONDataClass(
//    val data1: Int,
//    val data2: String,
//    val list: List<Int>
//    )
//data class MyJSONNestedDataClass(
//    val nested: MyJSONDataClass
//    )
//data class Adress(
//    val city: String,
//    val lat: Double,
//    val lon: Double
//    )
//data class Person(
//    val name: String,
//    val age: Int,
//    val favorites: List<String>,
//    val address: Adress
//    )

@JsonDeserialize(using = ComplexJSONDataDeserializer::class)
data class ComplexJSONData(
    val innerData: String,
    val data1: Int,
    val data2: String,
    val list: List<Int>
)
// 역직렬을 할 떄 힌트를 줌
class ComplexJSONDataDeserializer : StdDeserializer<ComplexJSONData>(
    ComplexJSONData::class.java
){
    // 실제로 여기서 역직렬을 함
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): ComplexJSONData {

        // node 객체 만들기 -> 얘가 complexJSONString의 최상단에 있는 {}를 가리키고 있음
        val node = p?.codec?.readTree<JsonNode>(p)

        // 얘는 nested를 가리키게 됨(객체는 형변환 필요 X)
        val nestedNode = node?.get("nested")

        // 얘는 nested 안의 inner_data를 가리키게 됨(이때 자료형 형변환 해야함, asText() -> String)
        val innerDataValue = nestedNode?.get("inner_data")?.asText()

        // TODO : data1이랑 data2 가져오기
        val innerNestedNode = nestedNode?.get("inner_nested")
        val innerNestedData1 = innerNestedNode?.get("data1")?.asInt()
        val innerNestedData2 = innerNestedNode?.get("data2")?.asText()

        // 리스트는 이렇게 추가하면 됨
        val list = mutableListOf<Int>()
        innerNestedNode?.get("list")?.elements()?.forEach {
            list.add(it.asInt())
        }

        return ComplexJSONData(
            innerDataValue!!,
            innerNestedData1!!,
            innerNestedData2!!,
            list
        )
    }

}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var mapper = jacksonObjectMapper()
/*

        // 역직렬 방법 1
        val jsonString = """
            {
                "data1": 1234,
                "data2": "Hello",
                "list": [1, 2, 3]
            }
        """.trimIndent()

        val d1 = mapper?.readValue<MyJSONDataClass>(jsonString)
        Log.d("mytag", d1.toString())

        val jsonString2 = """
            {
                "nested":{
                    "data1": 1234,
                    "data2": "Hello",
                    "list": [1, 2, 3]
                }
            }
        """.trimIndent()

        val d2 = mapper?.readValue<MyJSONNestedDataClass>(jsonString2)
        Log.d("mytag", d2.toString())

        val personString = """
            {
                "name": "John",
                "age": 20,
                "favorites": ["study", "game"],
                "address":{
                    "city": "Seoul",
                    "lat": 0.0,
                    "lon": 1.0
                }
            }
        """.trimIndent()

        val d3 = mapper?.readValue<Person>(personString)
        Log.d("mytag", d3.toString())
*/

        // 역직렬 방법 2(이걸 더 많이 사용)
        val complexJSONString = """
            {
                "nested":{
                    "inner_data": "Hello from inner",
                    "inner_nested": {
                        "data1": 1234,
                        "data2": "Hello",
                        "list": [1, 2, 3]
                    }
                }
            }
        """

        val complex = mapper?.readValue<ComplexJSONData>(complexJSONString)
        Log.d("mytag", complex.toString())
    }
}