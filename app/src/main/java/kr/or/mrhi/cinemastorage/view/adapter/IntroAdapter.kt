package kr.or.mrhi.cinemastorage.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kr.or.mrhi.cinemastorage.R

class IntroAdapter(private val context: Context) : PagerAdapter() {

    private var layoutInflater: LayoutInflater? = null

    /*배너를 배열로 저장*/
    private val banners = arrayOf(
        R.drawable.banner_first,
        R.drawable.banner_second,
        R.drawable.banner_third,
    )

    override fun getCount(): Int {
        return banners.size
    }
    /*PagerAdapter() 를상속받아 오버라이딩한 함수. veiw 객체를 만들어 리턴.*/
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as View
    }

    /*getSystemService(안드로이드가 제공하는 시스템-레벨 서비스를 요청)
    * 그 중에 레이아웃 리소스를 인플레이트 하는 서비스를 요청
    * 뷰페이저에 findViewById로 액티비티의 이미뷰에 배열로 저장된 배너이미지를 적용*/
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = layoutInflater?.inflate(R.layout.adapter_intro, container, false)
        val image = view?.findViewById<View>(R.id.iv_banner) as ImageView
        val viewPager = container as ViewPager

        image.setImageResource(banners[position])
        viewPager.addView(view, 0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }

}