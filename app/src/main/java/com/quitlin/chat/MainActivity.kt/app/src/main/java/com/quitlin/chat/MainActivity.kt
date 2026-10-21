
package com.quitlin.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val bg = Color(0xFF0A0E14)
private val panel = Color(0xFF151E2A)
private val mint = Color(0xFF55D6BE)
private val white = Color(0xFFF4F7FB)
private val muted = Color(0xFF9AA8BA)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { QuitlinApp() }
    }
}

@Composable
fun QuitlinApp() {
    var name by remember { mutableStateOf("") }
    var entered by remember { mutableStateOf(false) }
    var page by remember { mutableStateOf("الرئيسية") }
    var room by remember { mutableStateOf("الدردشة العامة") }
    var message by remember { mutableStateOf("") }
    var verifyRequested by remember { mutableStateOf(false) }
    val messages = remember { mutableStateListOf<String>() }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = mint,
            background = bg,
            surface = panel,
            onSurface = white
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = bg
        ) {
            if (!entered) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Q", color = mint, fontSize = 72.sp,
                        fontWeight = FontWeight.Black)
                    Text("كويتلن", color = white, fontSize = 34.sp,
                        fontWeight = FontWeight.Bold)
                    Text("دردشتك، أصدقاؤك، عالمك",
                        color = muted, fontSize = 15.sp)
                    Spacer(Modifier.height(28.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("اسم العرض") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(14.dp))
                    Button(
                        onClick = {
                            if (name.isBlank()) name = "ضيف كويتلن"
                            entered = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = mint,
                            contentColor = bg
                        )
                    ) { Text("دخول إلى كويتلن") }
                    Spacer(Modifier.height(12.dp))
                    Text("نسخة تجريبية", color = muted, fontSize = 12.sp)
                }
            } else {
                Column(Modifier.fillMaxSize()) {
                    Column(
                        Modifier.fillMaxWidth()
                            .background(panel).padding(16.dp)
                    ) {
                        Text("Q  كويتلن", color = mint,
                            fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        Text("أهلًا، $name", color = muted)
                    }

                    when (page) {
                        "الرئيسية" -> LazyColumn(
                            modifier = Modifier.weight(1f).padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Text("عالمك يبدأ هنا",
                                    color = white, fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold)
                                Text("اختر غرفة وابدأ التعارف",
                                    color = muted)
                            }
                            item {
                                Text("الغرف العامة", color = mint,
                                    fontSize = 19.sp)
                            }
                            items(listOf(
                                "🌍 الدردشة العامة",
                                "🌙 أصدقاء العرب",
                                "🎧 موسيقى وفن",
                                "🎮 الألعاب",
                                "✨ تعارف جديد"
                            )) { item ->
                                Row(
                                    Modifier.fillMaxWidth()
                                        .background(panel, RoundedCornerShape(16.dp))
                                        .clickable {
                                            room = item.substringAfter(" ")
                                            page = "الغرفة"
                                        }.padding(18.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(item, color = white,
                                        fontSize = 17.sp,
                                        modifier = Modifier.weight(1f))
                                    Text("دخول ←", color = mint)
                                }
                            }
                        }

                        "الغرفة" -> Column(
                            Modifier.weight(1f).padding(16.dp)
                        ) {
                            TextButton(onClick = { page = "الرئيسية" }) {
                                Text("← العودة للغرف")
                            }
                            Text(room, color = mint, fontSize = 22.sp,
                                fontWeight = FontWeight.Bold)
                            Text("رسائل هذه النسخة محفوظة مؤقتًا داخل التطبيق.",
                                color = muted, fontSize = 12.sp)
                            LazyColumn(
                                Modifier.weight(1f).fillMaxWidth().padding(vertical = 12.dp)
                            ) {
                                items(messages) { item ->
                                    Text(item, color = white,
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(vertical = 6.dp)
                                            .background(panel, RoundedCornerShape(12.dp))
                                            .padding(12.dp))
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    value = message,
                                    onValueChange = { message = it },
                                    modifier = Modifier.weight(1f),
                                    label = { Text("اكتب رسالة") }
                                )
                                TextButton(onClick = {
                                    if (message.isNotBlank()) {
                                        messages.add("$name: $message")
                                        message = ""
                                    }
                                }) { Text("إرسال") }
                            }
                        }

                        "الخاص" -> Column(
                            Modifier.weight(1f).padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("المحادثات الخاصة", color = white,
                                fontSize = 23.sp, fontWeight = FontWeight.Bold)
                            Text("ستحتاج المحادثات بين المستخدمين إلى ربط خادم.",
                                color = muted)
                            Text("لا توجد محادثات بعد", color = mint)
                        }

                        "التوثيق" -> Column(
                            Modifier.weight(1f).padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text("طلب التوثيق", color = white,
                                fontSize = 25.sp, fontWeight = FontWeight.Bold)
                            Text("قدّم طلبك ليقوم مالك التطبيق بمراجعته.",
                                color = muted)
                            Button(
                                onClick = { verifyRequested = true },
                                enabled = !verifyRequested,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = mint, contentColor = bg
                                )
                            ) {
                                Text(if (verifyRequested) "تم تقديم الطلب تجريبيًا"
                                else "إرسال طلب التوثيق")
                            }
                            Text("الطلب لا يصل إلى المالك فعليًا دون خادم.",
                                color = muted, fontSize = 12.sp)
                        }

                        "المالك" -> Column(
                            Modifier.weight(1f).padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text("لوحة تحكم المالك", color = white,
                                fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Text("طلبات التوثيق التجريبية", color = mint)
                            Text(
                                if (verifyRequested) "يوجد طلب تجريبي"
                                else "لا توجد طلبات",
                                color = muted
                            )
                            Text("تنبيه: هذه لوحة أولية وليست محمية بصلاحيات إدارية.",
                                color = muted, fontSize = 12.sp)
                        }
                    }

                    Row(
                        Modifier.fillMaxWidth().background(panel)
                            .navigationBarsPadding().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf("الرئيسية", "الخاص", "التوثيق", "المالك").forEach {
                            Text(
                                it,
                                color = if (page == it) mint else muted,
                                modifier = Modifier.clickable { page = it }
                                    .padding(8.dp),
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
