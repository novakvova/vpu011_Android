using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using WebShopApi.Abastract;
using WebShopApi.Constants;
using WebShopApi.Data.Entities.Identity;
using WebShopApi.Helpers;
using WebShopApi.Models;

namespace WebShopApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AccountController : ControllerBase
    {
        private readonly UserManager<UserEntity> _userManager;
        private readonly IJwtTokenService _jwtTokenService;
        public AccountController(UserManager<UserEntity> userManager, IJwtTokenService jwtTokenService)
        {
            _userManager = userManager;
            _jwtTokenService = jwtTokenService;
        }
        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] LoginViewModel model)
        {
            var user = await _userManager.FindByEmailAsync(model.Email);
            if (user == null)
            {
                return BadRequest();
            }
            var isPasswordValid = await _userManager.CheckPasswordAsync(user, model.Password);
            if (!isPasswordValid) 
            { 
                return BadRequest();
            }
            var token = await _jwtTokenService.CreateToken(user);
            return Ok(token);
        }
        [HttpPost("register")]
        public async Task<IActionResult> Register([FromBody] RegisterUserViewModel model)
        {
            var imageName = string.Empty;
            if(!string.IsNullOrEmpty(model.ImageBase64)) {
                imageName=ImageWorker.SaveImage(model.ImageBase64);
            }
            var user = new UserEntity
            {
                LastName = model.LastName,
                Email = model.Email,
                FirstName = model.FirstName,
                UserName = model.Email,
                Image = imageName
            };
            var result = await _userManager.CreateAsync(user, model.Password);
            if(result.Succeeded)
            {
                result = await _userManager.AddToRoleAsync(user, Roles.User);
                return Ok();
            }
            return BadRequest();
        }
    }
}
