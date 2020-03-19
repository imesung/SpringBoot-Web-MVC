package mesung.whiteship.demowebmvc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//스프링에서 제공해주는 JUnit용 Runner로서, 내부적으로 애플리케이션 컨텍스트도 만들어준다.
@RunWith(SpringRunner.class)
@WebMvcTest
public class SampleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void helloTestGet() throws Exception {
        mockMvc.perform(get("/hello"))
                .andDo(print()) //요청과 응답을 출력
                .andExpect(status().isOk()) //status 값이 isOk를 나오기를 기대
                .andExpect(content().string("hello"));  //본문에 있는 문자열이 hello로 나타날 것이다.
    }

    @Test
    public void helloTestPost() throws Exception {
        mockMvc.perform(post("/hello"))
                .andDo(print()) //요청과 응답을 출력
                .andExpect(status().isMethodNotAllowed()) //status 값 확인
                .andExpect(content().string("hello"));  //본문에 있는 문자열이 hello로 나타날 것이다.
    }


}