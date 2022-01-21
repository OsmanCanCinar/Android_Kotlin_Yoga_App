package com.osmancancinar.yogaapp.utils

//////////////////////////////////////////////

/*
    private val customPreferences: CustomSharedPreferences by lazy {
        CustomSharedPreferences(app)
    }

    fun saveToPreferences(meditationCategoryList: ArrayList<MeditationCategory>) {
        val gson = Gson()
        val list_name: String = gson.toJson(meditationCategoryList)
        println(list_name)
        customPreferences.clearList()
        customPreferences.saveList(list_name)
    }
 */

//////////////////////////////////////////////

/*
    private val customPreferences = CustomSharedPreferences(getApplication())
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L // 5 min in Nano Seconds

    fun refreshData(){
        val updateTime = customPreferences.getTime()
        if ((updateTime == null) || (updateTime == 0L) || ((System.nanoTime() - updateTime) >= refreshTime)) {
            displayMeditationCategoriesList()
        } else {
            getAllCategories()
        }
    }
*/

//////////////////////////////////////////////

/*
private fun createNotification() {
        val notificationManager: NotificationManager = requireContext().applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder: NotificationCompat.Builder
        val notificationChannel: NotificationChannel
        val channelID = "MeditationNotification"
        val channelName = "NotificationAction"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                description = "you can pause"
            }
            notificationManager.createNotificationChannel(notificationChannel)
        }

        var intent = Intent(requireContext().applicationContext, HomeActivity::class.java)
        var pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        notificationBuilder = NotificationCompat.Builder(requireContext().applicationContext, channelID)
        notificationBuilder.apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle("Normal Notice")
            setContentText("this a notification")
            setAutoCancel(true)
        }

        notificationManager.notify(100,notificationBuilder.build())
    }
 */

//////////////////////////////////////////////
