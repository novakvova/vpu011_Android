using AutoMapper;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using WebShopApi.Data;
using WebShopApi.Data.Entities;
using WebShopApi.Data.Entities.Identity;
using WebShopApi.Helpers;
using WebShopApi.Models;

namespace WebShopApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [Authorize]
    public class CategoriesController : ControllerBase
    {
        private readonly IMapper _mapper;
        private readonly AppEFContext _appEFContext;
        private readonly UserManager<UserEntity> _userManager;

        public CategoriesController(IMapper mapper, AppEFContext appEFContext,
            UserManager<UserEntity> userManager)
        {
            _mapper = mapper;
            _appEFContext = appEFContext;
            _userManager = userManager;
        }

        [HttpGet("list")]
        public async Task<IActionResult> Get()
        {
            string userName = User.Claims.First().Value;
            var user = await _userManager.FindByEmailAsync(userName);
            var model = await _appEFContext.Categories
                .Where(x=>x.isDelete == false)
                .OrderBy(x=>x.Priority)
                .Select(x=>_mapper.Map<CategoryItemViewModel>(x))
                .ToListAsync();
            return Ok(model);
        }

        [HttpPost]
        public async Task<IActionResult> Create([FromBody] CategoryCreateItemVM model)
        {
            try
            {
                var category = _mapper.Map<CategoryEntity>(model);
                category.Image = ImageWorker.SaveImage(model.ImageBase64);
                await _appEFContext.Categories.AddAsync(category);
                await _appEFContext.SaveChangesAsync();
                return Ok(_mapper.Map<CategoryItemViewModel>(category));
            }
            catch (Exception ex)
            {
                return BadRequest(new { error=ex.Message });
            }
        }
        [HttpPut("update")]
        public async Task<IActionResult> Put([FromBody] CategoryUpdateeItemVM model)
        {
            var cat = await _appEFContext.Categories.FindAsync(model.Id);
            if (cat == null)
                return NotFound();
            else
            {
                cat.Name= model.Name;
                cat.Description= model.Description;
                cat.Priority = model.Priority;
                if(!string.IsNullOrEmpty(model.ImageBase64))
                {
                    ImageWorker.RemoveImage(cat.Image);
                    cat.Image = ImageWorker.SaveImage(model.ImageBase64);
                }
                _appEFContext.Update(cat);
                _appEFContext.SaveChanges();
            }
            return Ok();
        }
        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {
            var category = await _appEFContext.Categories.FindAsync(id);
            if (category is null)
                return NotFound();
            else
            {
                category.isDelete = true;
                _appEFContext.SaveChanges();
                return Ok();
            }
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> GetById(int id)
        {
            var category = await _appEFContext.Categories.FindAsync(id);
            if (category is null)
                return NotFound();

            return Ok(_mapper.Map<CategoryItemViewModel>(category));

        }
    }
}
