# <img src="https://raw.githubusercontent.com/bobocode-projects/resources/master/image/logo_transparent_background.png" height=50/>Bean Post Processor exercise
Improve your *Spring* advanced skills 💪
### Task
Implement automatic String trimming functionality. So if I pass  "    Hello  "  as an argument to my bean’s method, or return it from the bean’s method, it gets automatically trimmed to "Hello".

- create annotation @Trimmed that you can put on class

- create annotation @EnableStringTrimming that will enable automatic String trimming for all beans that are annotated with @Trimmed

- create TrimmedAnnotationBeanPostProcessor that will check for beans that are marked with @Trimmed,

- create a proxy of those classes, and override methods. Proxy methods should:

  - trim all String arguments

  - trim all String return values

- extract  TrimmedAnnotationBeanPostProcessor  into a separate StringTrimmingConfiguration that is imported by @EnableStringTrimming

To verify your configuration, run `BeanPostProcessorTest.java`

 
### Pre-conditions ❗
You're supposed to be familiar with *Spring*

### How to start ❓
* Just clone the repository and start implementing the **todo** section, verify your changes by running tests
* If you don't have enough knowledge about this domain, check out the [links below](#Related-materials-ℹ)
* Don't worry if you got stuck, checkout the **exercise/completed** branch and see the final implementation
 
### Related materials ℹ
 todo

---
#### 🆕 First time here? – [See Introduction](https://github.com/bobocode-projects/java-fundamentals-course/tree/main/0-0-intro#introduction)

##
<div align="center"><img src="https://raw.githubusercontent.com/bobocode-projects/resources/master/animation/GitHub%20Star_3.gif" height=50/></div>