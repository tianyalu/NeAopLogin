# NeAopLogin AOP之集中式登录架构设计
## 一、理论
![image](https://github.com/tianyalu/NeAopLogin/raw/master/show/aop_login_structure1.png)  
![image](https://github.com/tianyalu/NeAopLogin/raw/master/show/aop_login_structure2.png)  

## 二、实操
> PointCut: 切入点(通过使用一些特定的表达式过滤出来的想要切入Advice的连接点)  
> Advice: 通知（向切入点中注入的代码的一种实现方法）[Before,After,Around->我们的代码在切入点处执行的时机]  
> Joint Point: 连接点（所有的目标方法都是连接点）  
### 2.1 注解类
`ClickBehavior`:  
```java
//用户点击痕迹（行为统计）
@Target(ElementType.METHOD) //目标作用在方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface ClickBehavior {
    String value();
}
```
`LoginCheck`:  
```java
//用户登录检查
@Target(ElementType.METHOD) //目标作用在方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginCheck {
}
```
### 2.2 切面类
`ClickBehaviorAspect`:  
```java
@Aspect //定义切面类
public class ClickBehaviorAspect {
    private static final String TAG = "sty --> ";

    // 1.应用中用到了哪些注解，放到当前的切入点进行处理（找到需要处理的切入点）
    // execution, 以方法执行时作为切点，触发Aspect类
    // * *(..)) 可以处理ClickBehavior这个类所有的方法
    @Pointcut("execution(@com.sty.ne.aoplogin.annotation.ClickBehavior * *(..))")
    public void methodPointCut(){}

    // 2.对切入点如何处理
    @Around("methodPointCut()")
    public Object jointPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取签名方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        // 获取方法所属的类名
        String className = methodSignature.getDeclaringType().getSimpleName();

        // 获取方法名
        String methodName = methodSignature.getName();

        // 获取方法的注解值（需要统计的用户行为）
        String funName = methodSignature.getMethod().getAnnotation(ClickBehavior.class).value();

        // 统计方法的执行时间，统计用户点击某功能行为。（存储到本地，没过X天上传到服务器）
        long begin = System.currentTimeMillis();
        Log.i(TAG, "ClickBehavior Method Start >>> ");
        Object result = joinPoint.proceed(); //MainActivity中切面的方法
        long duration = System.currentTimeMillis() - begin;
        Log.i(TAG, "ClickBehavior Method End >>> ");
        Log.i(TAG, String.format("统计了：%s 功能，在 %s 类中的 %s 方法，用时 %d ms",
                funName, className, methodName, duration));

        return result;
    }
}
```
`LoginCheckAspect`:  
```java
@Aspect //定义切面类
public class LoginCheckAspect {
    private static final String TAG = "sty --> ";

    // 1.应用中用到了哪些注解，放到当前的切入点进行处理（找到需要处理的切入点）
    // execution, 以方法执行时作为切点，触发Aspect类
    // * *(..)) 可以处理ClickBehavior这个类所有的方法
    @Pointcut("execution(@com.sty.ne.aoplogin.annotation.LoginCheck * *(..))")
    public void methodPointCut() {
    }

    // 2.对切入点如何处理
    @Around("methodPointCut()")
    public Object jointPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Context context = (Context) joinPoint.getThis();
        if (false) { // 从SharedPreferences中读取
            Log.i(TAG, "检测到已登录");
            return joinPoint.proceed();  //继续执行切面方法
        } else {
            Log.i(TAG, "检测到未登录");
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, LoginActivity.class));
            return null; // 不再执行方法（切入点）
        }
    }
}
```
### 2.3 `MainActivity`类
```java
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "sty--->";
    private Button btnLogin;
    private Button btnArea;
    private Button btnCoupon;
    private Button btnScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        btnLogin = findViewById(R.id.btn_login);
        btnArea = findViewById(R.id.btn_area);
        btnCoupon = findViewById(R.id.btn_coupon);
        btnScore = findViewById(R.id.btn_score);

        btnLogin.setOnClickListener(this);
        btnArea.setOnClickListener(this);
        btnCoupon.setOnClickListener(this);
        btnScore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                onBtnLoginClicked();
                break;
            case R.id.btn_area:
                onBtnAreaClicked();
                break;
            case R.id.btn_coupon:
                onBtnCouponClicked();
                break;
            case R.id.btn_score:
                onBtnScoreClicked();
                break;
            default:
                break;
        }
    }

    //登录点击事件（用户行为统计）
    @ClickBehavior("登录")
    private void onBtnLoginClicked() {
        Log.i(TAG, "模拟接口请求...验证通过，登录成功");
    }
    //用户行为统计
    @ClickBehavior("我的专区")
    @LoginCheck
    private void onBtnAreaClicked() {
        Log.i(TAG, "开始跳转到 -> 我的专区 Activity");
        startActivity(new Intent(this, OtherActivity.class));
    }
    //用户行为统计
    @ClickBehavior("我的优惠券")
    @LoginCheck
    private void onBtnCouponClicked() {
        Log.i(TAG, "开始跳转到 -> 我的优惠券 Activity");
        startActivity(new Intent(this, OtherActivity.class));
    }
    //用户行为统计
    @ClickBehavior("我的积分")
    @LoginCheck
    private void onBtnScoreClicked() {
        Log.i(TAG, "开始跳转到 -> 我的积分 Activity");
        startActivity(new Intent(this, OtherActivity.class));
    }
}
```