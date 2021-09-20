package com.baiyu.androidx.basicmodule.util

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BindingActivity<T : ViewDataBinding> constructor(
  @LayoutRes private val contentLayoutId: Int
) : AppCompatActivity() {


  /**
   * A data-binding property will be initialized before being called [onCreate].
   * And inflates using the [contentLayoutId] as a content view for activities.
   */
  @BindingOnly
  protected val binding: T by lazy(LazyThreadSafetyMode.NONE) {
    DataBindingUtil.setContentView(this, contentLayoutId)
  }

  /**
   * An executable inline binding function that receives a binding receiver in lambda.
   *
   * @param block A lambda block will be executed with the binding receiver.
   * @return T A generic class that extends [ViewDataBinding] and generated by DataBinding on compile time.
   */
  @BindingOnly
  protected inline fun binding(block: T.() -> Unit): T {
    return binding.apply(block)
  }

  /**
   * Ensures the [binding] property should be executed before being called [onCreate].
   */
  init {
    addOnContextAvailableListener {
      binding.notifyChange()
    }
  }

  /**
   * Removes binding listeners to expression variables and destroys the [binding] backing property for preventing
   * leaking the [ViewDataBinding] that references the Context.
   */
  override fun onDestroy() {
    super.onDestroy()
    binding.unbind()
  }
}
