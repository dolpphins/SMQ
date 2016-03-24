##项目名称
####快踩我

##项目编码
**UTF-8**

##开发环境
**Android Studio1.2 + JDK1.8**

##运行环境
**最小Android SDK版本：4.0**

**目标Android SDK版本：5.1**

##项目包结构

* com.lym.stamp.base
    *  BaseActivity
    *  BaseDialog
    *  BaseGameActivity
* com.lym.stamp.bean
    * Crash
    * Feedback
    * Square
    * TUSer
    * UpdateBean
* com.lym.stamp.calculator
    * ColorCalculator
    * DigitCalculator
    * ICalculate
* com.lym.stamp.bean.color.generator
    *  BaseColorGenerator
    *  IColorGenerator
*  com.lym.stamp.bean.color.generator.impl
    * AverageColorGenerator
    * RandomColorGenerator
* com.lym.stamp.conf
    * ColorsKeeper
    * SpConfig
* com.lym.stamp.crash
    *  CrashHandler
* com.lym.stamp.dialog
    *  GameOverDialog
    *  GamePauseDialog
    *  GameReadyDialog
* com.lym.stamp.digit.generator
    *  BaseDigitGenerator
    *  IDigitGenerator
* com.lym.stamp.digit.generator.impl
    * RandomDigitGenerator 
* com.lym.stamp.generator
    * BaseSquareGenerator
    * ISquareGenerator
    * SquareGeneratorConfiguration
* com.lym.stamp.generator.impl
    * DefaultSquareGenerator 
* com.lym.stamp.screen
    * DisplayUitls 
* com.lym.stamp.service
    * CrashService 
* com.lym.stamp.side
    * BaseIdiomGenerator
    * SideGenerator
* com.lym.stamp.side.impl
    * RandomIdiomGenerator 
* com.lym.stamp.ui
    *  ColorsActivity
    *  DigitsActivity
    *  EncryptHelper
    *  HelpActivity
    *  MainActivity
    *  SettingsActivity
    *  SideActivity
* com.lym.stamp.utils
    * AppUtils
    * CompatMethod
    * ImageUtil
    * IoUtils
    * NetworkHelper
    * SharePreferencesManager
    * TextUtil
* com.lym.stamp.widget
    *  DropSurfaceView
    *  DropViewConfiguration
* com.lym.stamp
    * AccessTokenKeeper
    * App
    * NetworkDataManager
    * ScoresManager
    * UserManager
